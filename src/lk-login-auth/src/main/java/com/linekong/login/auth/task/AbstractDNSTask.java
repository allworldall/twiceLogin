package com.linekong.login.auth.task;

import com.linekong.login.auth.utils.PropertyConfigUtil;
import com.linekong.login.auth.utils.log.LoggerUtil;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/22
 * Time: 10:27
 */
public abstract class AbstractDNSTask extends TimerTask implements IDNSTask {

    /**
     * 响应时间大于此值时，切换ip
     */
    protected static final int INTERVAL;
    /**
     * 自定义超时时间，超过该时间则认为url或ip不可用
     */
    protected static final int TIMEOUT = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.timeout").toString());
    /**
     * 重连间隔时间
     */
    protected static final int REC_TIME = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.rectime").toString()) * 60 * 1000;

    /**
     * 默认重连间隔时间
     */
    protected static final int REC_TIME_DEFAULT = 5 * 60 * 1000;

    /**
     * 重新解析所有域名间隔时间
     */
    protected static final int REFRESH_TIME = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.reftime").toString()) * 60 * 1000;

    /**
     * 是否开启ip调度
     */
    protected static final boolean AOTU_DISPATCH = Boolean.valueOf(PropertyConfigUtil.getContextProperty("timetask.auto_dispatch").toString());

    /**
     * ip调度线程池核心池的大小
     */
    protected static final int CORE_POOL_SIZE;

    /**
     * ip调度线程池的最大线程数
     */
    protected static final int MAX_SIZE;

    /**
     * ip调度线程池多余的空闲线程等待新任务的最长时间
     */
    protected static final long MAX_WAIT;

    /**
     * ip调度任务队列最大值
     */
    protected static final int MAX_TASK;

    /**
     * 缓存域名解析信息
     */
    protected static ConcurrentHashMap<String, TreeMap<Long, String>> ipMap = new ConcurrentHashMap<>();

    /**
     * 缓存ip对应的域名
     */
    protected static ConcurrentHashMap<String, String> hostMap = new ConcurrentHashMap<>();

    /**
     * 初始化时连接超时的url
     */
    protected static ConcurrentSkipListSet<String> timeoutUrlSet = new ConcurrentSkipListSet<>();

    /**
     * 重置锁
     */
    protected static volatile Map<String, Boolean> lockMap = new HashMap<>();

    static {
        //仅当开启ip调度时设置参数值
        if (AOTU_DISPATCH) {
            INTERVAL = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.interval").toString());
            CORE_POOL_SIZE = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.corePoolSize").toString());
            MAX_SIZE = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.maxSize").toString());
            MAX_WAIT = Long.valueOf(PropertyConfigUtil.getContextProperty("timetask.maxWait").toString());
            MAX_TASK = Integer.valueOf(PropertyConfigUtil.getContextProperty("timetask.maxTask").toString());
        } else {
            INTERVAL = 0;
            CORE_POOL_SIZE = 0;
            MAX_SIZE = 0;
            MAX_WAIT = 0;
            MAX_TASK = 0;
        }
    }

    @Override
    public ConcurrentHashMap<String, TreeMap<Long, String>> getIpMap(){
        return ipMap;
    }

    @Override
    public ConcurrentHashMap<String, String> getHostMap(){
        return hostMap;
    }

    /**
     * 解析单个域名
     * @param url
     * @return
     */
    protected static TreeMap<Long, String> getIpAdrrByName(String url){
        String useHost = URI.create(url).getHost();
        TreeMap<Long, String> treeMap = new TreeMap<>();
        long start;
        long end;
        boolean isOpen;
        String useUrl;
        try {
            InetAddress[] ips= InetAddress.getAllByName(useHost);
            for (InetAddress ip: ips) {
                start = System.currentTimeMillis();
                isOpen = ip.isReachable(TIMEOUT);
                end = System.currentTimeMillis();
                if (isOpen) {
                    useUrl = url.replace(useHost, ip.getHostAddress());
                    setValue(treeMap, useUrl, (end - start));
                    hostMap.put(useUrl, useHost);
                } else {
                    LoggerUtil.info(AbstractDNSTask.class, "ip连接超时，ip：" + ip.getHostAddress() + "，URL：" + url);
                }
            }
            if (MapUtils.isEmpty(treeMap)) {
                timeoutUrlSet.add(url);
                LoggerUtil.info(AbstractDNSTask.class, "URL连接超时，解析失败，URL：" + url);
            }
        } catch (IOException e) {
            LoggerUtil.error(AbstractDNSTask.class, "解析域名错误 error: " + e.getMessage(), e);
        }
        return treeMap;
    }

    /**
     * 处理key值重复的情况
     * @param treeMap
     * @param value
     * @param time
     */
    protected static void setValue(TreeMap<Long, String> treeMap, String value, Long time){
        if(treeMap.containsKey(time)){
            time ++;
            setValue(treeMap, value, time);
        } else {
            treeMap.put(time, value);
        }
    }
}
