package com.linekong.login.auth.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.linekong.login.auth.service.IChannelSignService;
import com.linekong.login.auth.service.IChannelValidateService;
import com.linekong.login.auth.utils.log.LoggerUtil;

public class CreateServiceProxy {

    private static volatile IChannelValidateService channelValidateServiceProxy;
    
    private static volatile IChannelSignService channelSignServiceProxy;

	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Class<T> cls, InvocationHandler handler){
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{cls}, handler);
	}

    public static IChannelValidateService getChannelValidateServiceProxy(InvocationHandler handler) {
        if(channelValidateServiceProxy == null){
            synchronized (CreateServiceProxy.class) {
                if (channelValidateServiceProxy == null) {
                    channelValidateServiceProxy = CreateServiceProxy.getProxy(IChannelValidateService.class, handler);
                    LoggerUtil.info(CreateServiceProxy.class, "ChannelValidateService has been created");
                }
            }
        }
        return channelValidateServiceProxy;
    }
    
    public static IChannelSignService getChannelSignServiceProxy(InvocationHandler handler) {
        if(channelSignServiceProxy == null){
            synchronized (CreateServiceProxy.class) {
                if (channelSignServiceProxy == null) {
                	channelSignServiceProxy = CreateServiceProxy.getProxy(IChannelSignService.class, handler);
                    LoggerUtil.info(CreateServiceProxy.class, "ChannelSignService has been created");
                }
            }
        }
        return channelSignServiceProxy;
    }

}
