package com.linekong.login.auth.task;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/22
 * Time: 10:18
 */
public interface IDNSTask{
    /**
     * 启动解析域名任务
     */
    void parseDomainName();

    /**
     * 启动重连超时域名任务
     */
    void reParseTimeoutUrl();

    /**
     * 获取域名解析后的map
     * @return
     */
    ConcurrentHashMap<String, TreeMap<Long, String>> getIpMap();

    /**
     * 获取域名解析后的map
     * @return
     */
    ConcurrentHashMap<String, String> getHostMap();

    /**
     * 获取当前域名下，响应最快的ip
     * @param url
     * @return
     */
    String getFastIp(String url);

    /**
     * 重置当前域名下，ip的排序
     * @param url
     */
    void resetIp(String url, long executeTime);

}
