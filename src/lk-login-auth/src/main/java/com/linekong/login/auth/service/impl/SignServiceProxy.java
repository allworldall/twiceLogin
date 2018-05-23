package com.linekong.login.auth.service.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linekong.login.auth.service.IChannelSignHandler;

@Service
public class SignServiceProxy implements InvocationHandler {

	@Autowired
	private IChannelSignHandler iChannelSignHandler;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
        Method m = IChannelSignHandler.class.getDeclaredMethod(method.getName(), method.getParameterTypes());

		return m.invoke(iChannelSignHandler, args);
	}

}
