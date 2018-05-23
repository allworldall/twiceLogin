package com.linekong.login.auth.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/7
 * Time: 9:59
 */
@Component
public class LockUtil {

    private final ConcurrentHashMap accounts = new ConcurrentHashMap();

    public synchronized void lock(String account) throws InterruptedException{
        while ( accounts.containsKey(account) ){
            wait();
        }
        accounts.put(account, true);
    }

    public synchronized void release(String account) {
        accounts.remove(account);
        notify();
    }
}
