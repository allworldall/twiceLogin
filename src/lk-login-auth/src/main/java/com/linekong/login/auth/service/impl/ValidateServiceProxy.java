package com.linekong.login.auth.service.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linekong.login.auth.service.IChannelValidateHandler;

@Service
public class ValidateServiceProxy implements InvocationHandler{

	@Autowired
	private IChannelValidateHandler iChannelValidateHandler;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
        Method m = IChannelValidateHandler.class.getDeclaredMethod(method.getName(), method.getParameterTypes());

		return m.invoke(iChannelValidateHandler, args);
	}
}
