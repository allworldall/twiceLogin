package com.linekong.login.auth.task.impl;

import com.linekong.login.auth.dao.OperationDataBaseDao;
import com.linekong.login.auth.task.AbstractDNSTask;
import com.linekong.login.auth.utils.log.LoggerUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/22
 * Time: 10:14
 */
@Component
public class DNSTaskImpl extends AbstractDNSTask{

    @Autowired
    private OperationDataBaseDao operationDataBaseDao;

    /**
     * ip调度线程池
     */
    private static final ExecutorService resetIpPool;

    static {
        if (AOTU_DISPATCH) {
            resetIpPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_SIZE, MAX_WAIT, TimeUnit.SECONDS, new LinkedBlockingQueue(MAX_TASK), new ThreadPoolExecutor.DiscardPolicy());
        } else {
            resetIpPool = null;
        }
    }

    @Override
    public void parseDomainName() {
        new Timer().schedule(this, 0, REFRESH_TIME);
        LoggerUtil.info(DNSTaskImpl.class, "启动域名解析定时任务");
    }

    @Override
    public void reParseTimeoutUrl() {
        new Timer().schedule(new RecTask(), REC_TIME, REC_TIME > REC_TIME_DEFAULT ? REC_TIME : REC_TIME_DEFAULT);
        LoggerUtil.info(DNSTaskImpl.class, "启动重连超时域名定时任务");
    }

    @Override
    public String getFastIp(String url) {
        TreeMap<Long, String> treeMap = ipMap.get(url);
        return MapUtils.isNotEmpty(treeMap) ? treeMap.firstEntry().getValue() : url;
    }

    @Override
    public void resetIp(String url, long executeTime) {
        if (StringUtils.isBlank(url)) {
            return;
        }

        TreeMap<Long, String> treeMap = ipMap.get(url);
        //排除url连接失败或者不存在的情况
        if (MapUtils.isEmpty(treeMap)) {
            return;
        }

        Long fastTime = treeMap.firstKey();

        //如果执行时间大于设置的切换ip时间
        if (AOTU_DISPATCH && executeTime > INTERVAL){
            //如果当前url没有正在重置
            if (!lockMap.containsKey(url)){
                //排除ip数量是1的情况也去做调整最快ip的操作
                if (executeTime >= TIMEOUT || treeMap.size() > 1){
                    reset(url, fastTime, executeTime);
                }
            }
        }
    }

    @Override
    public void run() {
        List<Object> urlList = operationDataBaseDao.queryAllVerifyUrl();
        if (CollectionUtils.isNotEmpty(urlList)) {
            ExecutorService executorService =  new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
            List<Future<TreeMap>> futureList = new ArrayList<>(urlList.size());
            Future<TreeMap> future = null;

            timeoutUrlSet.clear();

            for (Object url: urlList) {
                future = executorService.submit(new ParseTask(url.toString()));
                futureList.add(future);
            }
            executorService.shutdown();

            try {
                for (int i = 0; i < urlList.size(); i++) {
                   ipMap.put(urlList.get(i).toString(), futureList.get(i).get());
                }
                LoggerUtil.info(DNSTaskImpl.class, "域名解析完成");
            } catch (Exception e) {
                LoggerUtil.error(DNSTaskImpl.class, "获取域名解析结果错误, error：" + e.getMessage(), e);
            }

        } else {
            LoggerUtil.info(DNSTaskImpl.class, "没有需要解析的域名");
        }
    }

    /**
     * 重置ip
     * @param url
     * @param fastTime
     * @param executeTime
     */
    private /*synchronized*/ void reset(String url, long fastTime, long executeTime){
        if (!lockMap.containsKey(url)){
            lockMap.put(url, true);
            //如果执行时间大于等于超时时间, 重新解析域名
            if (executeTime >= TIMEOUT){
                resetIpPool.execute(new ResetTask(url));
            } else {//小于超时时间，重新调整最快ip
                resetIpPool.execute(new ResetTask2(url, fastTime + INTERVAL));
            }
        }
    }

    /**
     * 初始域名解析
     */
    private class ParseTask implements Callable<TreeMap> {
        String url;

        ParseTask(String url){
            this.url = url;
        }

        @Override
        public TreeMap call() throws Exception {
            return getIpAdrrByName(url);
        }
    }

    /**
     * 连接超时的情况重新解析域名
     */
    private class ResetTask implements Runnable {
        String url;

        ResetTask(String url){
            this.url = url;
        }

        @Override
        public void run() {
            ipMap.put(url, getIpAdrrByName(url));
            //释放锁
            lockMap.remove(url);
            LoggerUtil.info(DNSTaskImpl.class, "超时域名重新解析成功：" + url);
        }
    }

    /**
     * 响应时间过长的情况重新解析域名
     */
    private class ResetTask2 implements Runnable {
        String url;
        Long time;

        ResetTask2(String url, Long time){
            this.url = url;
            this.time = time;
        }

        @Override
        public void run() {
            //拷贝
            TreeMap<Long, String> treeMapClone = (TreeMap<Long, String>) ipMap.get(url).clone();
            //将真实值存入，原firstKey删除，treeMap自动排序
            String firstValue = treeMapClone.get(treeMapClone.firstKey());
            treeMapClone.remove(treeMapClone.firstKey());
            //如果key已存在
            setValue(treeMapClone, firstValue, time);
            //更新
            ipMap.put(url, treeMapClone);
            //释放锁
            lockMap.remove(url);
            LoggerUtil.info(DNSTaskImpl.class, "重新排序，URL：" + url);
        }
    }

    /**
     * 重新解析请求超时的url
     * (与ResetTask任务情况不同的是，ResetTask是对ipMap中已解析出对应ip的URL重新解析，而此任务是针对初始解析时就超时的URL)
     * 执行此任务时不必考虑url正在被ResetTask任务解析，因为timeoutUrlSet中存储的都是解析失败的url
     */
    private class RecTask extends TimerTask {

        @Override
        public void run() {
            Iterator<String> iterator = timeoutUrlSet.iterator();
            TreeMap<Long, String>  treeMap = null;
            String url = null;
            while (iterator.hasNext()) {
                url = iterator.next();
                treeMap = getIpAdrrByName(url);
                ipMap.put(url, treeMap);
                if (MapUtils.isNotEmpty(treeMap)) {
                    timeoutUrlSet.remove(url);
                    LoggerUtil.info(DNSTaskImpl.class, "重连成功，URL：" + url);
                }
            }
        }
    }

}
