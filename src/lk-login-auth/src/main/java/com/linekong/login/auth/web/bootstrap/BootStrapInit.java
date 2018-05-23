package com.linekong.login.auth.web.bootstrap;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/**
 * 定时任务启动入口
 * 	通过Spring ApplicationListener 来实现Spring容器加装完毕自动启动
 * @author fangming
 */
@Component("bootStrapInit")
public class BootStrapInit implements ApplicationListener<ContextRefreshedEvent>{

	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println(System.getenv("LK_LOGIN_AUTH") + File.separator + "log4j.properties");
		PropertyConfigurator.configure(System.getenv("LK_LOGIN_AUTH") + File.separator + "log4j.properties");
		
	}

}
