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

import com.linekong.login.auth.pojo.ChannelSignBean;
import com.linekong.login.auth.pojo.SignRuleBean;
import com.linekong.login.auth.pojo.SignServiceBean;

public class SignContext {
private static final String XMLPATH = "channelSignServiceConfig.xml";
	
	private static final String NAMESPACE = "namespace";
	
	private static final String CHANNEL = "channel";
	
	private static final String ID = "id";
	
	private static final String GROUP = "group";
	
	private static final String SIGN = "sign";
	
	private static final String VERSION = "version";
	
	private static final String REF = "ref";
	
	private static final String REQUIRED = "required";
	
	private static final String DEFAULT = "default";
	
	private static final String REQUEST = "request";
	
	private static final String CONFIG = "config";
	
	private static final String CALLBACK = "callback";
	
	private static final String CLASS = "class";
	
	private static SignServiceBean ssb = new SignServiceBean();
	
	/**
	 * 
	 * @param channelId
	 * @return
	 */
	public static ChannelSignBean getChannelSignBean(Long channelId){
		ChannelSignBean bean = null;
		for (ChannelSignBean SignBean : ssb.getChannelSignList()) {
			if(channelId == SignBean.getChannelId()){
				bean = SignBean;
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
			
			List<ChannelSignBean> channelSignList = new ArrayList<ChannelSignBean>();
			
			ssb.setNamespace(root.attributeValue(NAMESPACE));
			
			Iterator<Element> channelIterator = root.elementIterator(CHANNEL);
			Element channel = null;
			ChannelSignBean channelSignBean = null;
			Iterator<Element> signIterator = null;
			while(channelIterator.hasNext()){
				
				channelSignBean = new ChannelSignBean(ssb);
				channel = channelIterator.next();
				channelSignBean.setChannelId(Long.valueOf(channel.attributeValue(ID)));
				channelSignBean.setGroup(channel.attributeValue(GROUP));
				
				signIterator = channel.elementIterator(SIGN);
				setSign(signIterator, channelSignBean);
				
				channelSignList.add(channelSignBean);
			}
			
			ssb.setChannelSignList(channelSignList);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取渠道下所有版本
	 * @param signIterator
	 * @param channelValidateBean
	 */
	private static void setSign(Iterator<Element> signIterator, ChannelSignBean channelSignBean){
		Element valid = null;
		Iterator<Element> requiredIterator = null;
		SignRuleBean roleBean = null;
		List<SignRuleBean> roleList = new ArrayList<SignRuleBean>();
		while(signIterator.hasNext()){
			valid = signIterator.next();
			
			roleBean = new SignRuleBean(channelSignBean);
			
			roleBean.setVersion(valid.attributeValue(VERSION));
			
			//如果配置引用的版本验证
			if(StringUtils.isNotBlank(valid.attributeValue(REF))){
				roleBean.setRef(valid.attributeValue(REF));
				roleList.add(roleBean);
				continue;
			}
			
			roleBean.setVerifier(getClassPath(channelSignBean, valid.element(CALLBACK).attributeValue(CLASS)));
			
			requiredIterator = valid.elementIterator(REQUIRED);
			
			setRequiredParam(requiredIterator, roleBean);
			
			roleList.add(roleBean);
			
			if(valid.attribute(DEFAULT) != null && Boolean.valueOf(valid.attributeValue(DEFAULT))){
				channelSignBean.setDefaultRole(roleBean);
			}
		}
		
		channelSignBean.setRoleList(roleList);
	}
	
	/**
	 * 获取当前版本下必须的参数名
	 * @param requiredIterator
	 * @param roleBean
	 */
	private static void setRequiredParam(Iterator<Element> requiredIterator, SignRuleBean roleBean){
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
	private static void setParams(Iterator<Element> paramIterator, SignRuleBean roleBean){
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
	private static String getClassPath(ChannelSignBean channelSignBean, String className){
		StringBuilder sb = new StringBuilder();
		sb.append(ssb.getNamespace()).append(".");
		sb.append(channelSignBean.getGroup()).append(".").append(className);
		return sb.toString();
	}
}
