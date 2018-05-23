/**
 * 
 */
package com.linekong.login.auth.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.linekong.login.auth.utils.log.LoggerUtil;
/**
 * @author Administrator
 *
 */
public class XmlUtil {
	@SuppressWarnings("rawtypes")
	public static Map<String, String> parseXml(String xmlString){
		Map<String, String> map = new HashMap<String, String>();
		try{

			Document document = DocumentHelper.parseText(xmlString);   
			Element employees=document.getRootElement();   
			for(Iterator i = employees.elementIterator(); i.hasNext();){   
				Element node = (Element) i.next();
				map.put(node.getQualifiedName(), node.getText());
			}
		}catch(Exception ex){
			LoggerUtil.error(XmlUtil.class, xmlString + ":" + ex.getMessage());
		}
		return map;
	}
}
