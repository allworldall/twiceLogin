package com.linekong.login.auth.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.linekong.login.auth.pojo.ChannelValidateBean;
import com.linekong.login.auth.pojo.ValidateRuleBean;
import com.linekong.login.auth.pojo.ValidateServiceBean;


public class ValidateContext {

	private static final String XMLPATH = "channelValidateServiceConfig.xml";
	
	private static final String NAMESPACE = "namespace";
	
	private static final String CHANNEL = "channel";
	
	private static final String ID = "id";
	
	private static final String GROUP = "group";
	
	private static final String VALIDATE = "validate";
	
	private static final String VERSION = "version";
	
	private static final String REF = "ref";
	
	private static final String REQUIRED = "required";
	
	private static final String DEFAULT = "default";
	
	private static final String REQUEST = "request";
	
	private static final String CONFIG = "config";
	
	private static final String CALLBACK = "callback";
	
	private static final String CLASS = "class";
	
	private static ValidateServiceBean vsb = new ValidateServiceBean();
	
	/**
	 * 
	 * @param channelId
	 * @return
	 */
	public static ChannelValidateBean getChannelValidateBean(Long channelId){
		ChannelValidateBean bean = null;
		for (ChannelValidateBean validateBean : vsb.getChannelValidateList()) {
			if(channelId == validateBean.getChannelId()){
				bean = validateBean;
				break;
			}
		}
		return bean;
	}
	
	/**
	 * 将xml文件中渠道验证service读入bean
	 */
	public static void readXmlToBean(String basePath){
		File file = new File(basePath.concat(XMLPATH));
		//创建SAXReader对象  
        SAXReader reader = new SAXReader();  
        //读取文件 转换成Document  
        try {
			Document document = reader.read(file);
			Element root = document.getRootElement();
			
			List<ChannelValidateBean> channelValidateList = new ArrayList<ChannelValidateBean>();
			
			vsb.setNamespace(root.attributeValue(NAMESPACE));
			
			Iterator<Element> channelIterator = root.elementIterator(CHANNEL);
			Element channel = null;
			ChannelValidateBean channelValidateBean = null;
			Iterator<Element> validIterator = null;
			while(channelIterator.hasNext()){
				
				channelValidateBean = new ChannelValidateBean(vsb);
				channel = channelIterator.next();
				channelValidateBean.setChannelId(Long.valueOf(channel.attributeValue(ID)));
				channelValidateBean.setGroup(channel.attributeValue(GROUP));
				
				validIterator = channel.elementIterator(VALIDATE);
				setValidate(validIterator, channelValidateBean);
				
				channelValidateList.add(channelValidateBean);
			}
			
			vsb.setChannelValidateList(channelValidateList);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取渠道下所有版本
	 * @param validIterator
	 * @param channelValidateBean
	 */
	private static void setValidate(Iterator<Element> validIterator, ChannelValidateBean channelValidateBean){
		Element valid = null;
		Iterator<Element> requiredIterator = null;
		ValidateRuleBean roleBean = null;
		List<ValidateRuleBean> roleList = new ArrayList<ValidateRuleBean>();
		while(validIterator.hasNext()){
			valid = validIterator.next();
			
			roleBean = new ValidateRuleBean(channelValidateBean);
			
			roleBean.setVersion(valid.attributeValue(VERSION));
			
			//如果配置引用的版本验证
			if(StringUtils.isNotBlank(valid.attributeValue(REF))){
				roleBean.setRef(valid.attributeValue(REF));
				roleList.add(roleBean);
				continue;
			}
			
			roleBean.setVerifier(getClassPath(channelValidateBean, valid.element(CALLBACK).attributeValue(CLASS)));
			
			requiredIterator = valid.elementIterator(REQUIRED);
			
			setRequiredParam(requiredIterator, roleBean);
			
			roleList.add(roleBean);
			
			if(valid.attribute(DEFAULT) != null && Boolean.valueOf(valid.attributeValue(DEFAULT))){
				channelValidateBean.setDefaultRole(roleBean);
			}
		}
		
		channelValidateBean.setRoleList(roleList);
	}
	
	/**
	 * 获取当前版本下必须的参数名
	 * @param requiredIterator
	 * @param roleBean
	 */
	private static void setRequiredParam(Iterator<Element> requiredIterator, ValidateRuleBean roleBean){
		Element required = null;
		
		Iterator<Element> paramIterator = null;
		
		while(requiredIterator.hasNext()){
			required = requiredIterator.next();
			paramIterator = required.elementIterator();
			
			setParams(paramIterator, roleBean);
		}
	}
	
	/**
	 * 写入必须的参数
	 * @param paramIterator
	 * @param roleBean
	 */
	private static void setParams(Iterator<Element> paramIterator, ValidateRuleBean roleBean){
		Element params = null;
		while(paramIterator.hasNext()){
			params = paramIterator.next();
			
			if(REQUEST.equals(params.getName())){
				roleBean.setRequestParamList(new ArrayList<String>(Arrays.asList(params.getTextTrim().split(","))));
			}
			
			if(CONFIG.equals(params.getName())){
				roleBean.setConfigParamList(new ArrayList<String>(Arrays.asList(params.getTextTrim().split(","))));
			}
		}
	}
	
	/**
	 * 获取类全名
	 * @param channelValidateBean
	 * @param className
	 * @return
	 */
	private static String getClassPath(ChannelValidateBean channelValidateBean, String className){
		StringBuilder sb = new StringBuilder();
		sb.append(vsb.getNamespace()).append(".");
		sb.append(channelValidateBean.getGroup()).append(".").append(className);
		return sb.toString();
	}
}
