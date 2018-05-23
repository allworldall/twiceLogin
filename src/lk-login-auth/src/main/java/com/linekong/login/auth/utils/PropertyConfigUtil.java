package com.linekong.login.auth.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/23
 * Time: 19:21
 */
public class PropertyConfigUtil extends PropertyPlaceholderConfigurer {

    private static Map<String, Object> propertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        propertiesMap = new HashMap<String, Object>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            propertiesMap.put(keyStr, value);
        }
    }

    public static Object getContextProperty(String name) {
        return propertiesMap.get(name);
    }

    public static Object setContextProperty(String name,Object value) {
        return propertiesMap.put(name, value);
    }
}
