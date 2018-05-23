package com.linekong.login.auth.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linekong.login.auth.dao.LoginTokenCacheOperationDao;
import com.linekong.login.auth.exception.NoSuchConfigParamException;
import com.linekong.login.auth.exception.NoSuchRequestParamException;
import com.linekong.login.auth.exception.NotFoundDefaultValidateServiceException;
import com.linekong.login.auth.pojo.ChannelSignBean;
import com.linekong.login.auth.pojo.SignRuleBean;
import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IChannelSignHandler;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.web.formBean.LoginSignFormBean;

@Service
public class ChannelSignHandler  implements IChannelSignHandler {
private static final String MSG = "%s is null ! ";
	
	@Autowired
	private ChannelServiceTemplate channelServiceTemplate;
    @Autowired
    private LoginTokenCacheOperationDao loginTokenCacheOperationDao;


	public Map<String, Object> sign(LoginSignFormBean loginSignFormBean, ChannelSignBean signBean) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> dataMap = new HashMap<String, Object>();

		resultMap.put("data", dataMap);

		try {
			//获取对应版本校验服务
			SignRuleBean roleBean = getRoleBean(loginSignFormBean, signBean);

			//获取数据库配置参数
            Map<String, String> channelInfo = getChannelInfo(roleBean, loginSignFormBean);

			//校验是否传递/配置必须的参数
			checkParam(loginSignFormBean, channelInfo, roleBean);


			//必传参数校验通过后，根据渠道签名生成规则生成签名，调用第三方API
            roleBean.getVerifier().sign(channelInfo, resultMap, loginSignFormBean, channelServiceTemplate);


			//返回结果
		} catch (NotFoundDefaultValidateServiceException e) {
			//未配置默认的版本处理服务
			LoggerUtil.error(ValidateServiceProxy.class, "channelName_" + signBean.getGroup() + ", " + e.getMessage());
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_VERSION);
		} catch (NoSuchRequestParamException e) {
			//未传递参数
			LoggerUtil.error(ValidateServiceProxy.class, "channelName_" + signBean.getGroup() + ", " + e.getMessage());
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_PARAM);
		} catch (NoSuchConfigParamException e) {
			//未配置参数
			LoggerUtil.error(ValidateServiceProxy.class, "channelName_" + signBean.getGroup() + ", " + e.getMessage());
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_PARAM_CFG);
		} catch (Exception e){
			LoggerUtil.error(ValidateServiceProxy.class, "channelName_" + signBean.getGroup() + " is error! param=" + JSONUtil.objToJsonString(loginSignFormBean) + ", " + e.getMessage(), e);
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR);
		}
		return resultMap;
	}

	/**
	 * 获取对应版本校验服务
	 * @param loginSignFormBean
	 * @param signBean
	 * @return
	 * @throws NotFoundDefaultValidateServiceException 
	 */
	private SignRuleBean getRoleBean(LoginSignFormBean loginSignFormBean, ChannelSignBean signBean) throws NotFoundDefaultValidateServiceException{
		List<SignRuleBean> roleList = signBean.getRoleList();
		SignRuleBean roleBean = null;
		
		for (SignRuleBean signRuleBean : roleList) {
			if(loginSignFormBean.getVersion().equals(signRuleBean.getVersion())){
				roleBean = signRuleBean;
				break;
			}
		}
		
		//如果获取到对应版本验证服务，判断此版本是否引用其他版本服务
		if(roleBean != null && StringUtils.isNotBlank(roleBean.getRef())){
			for (SignRuleBean signRuleBean : roleList) {
				if(roleBean.getRef().equals(signRuleBean.getVersion())){
					roleBean = signRuleBean;
					break;
				}
			}
			
		}
		
		if(roleBean == null){
			roleBean = signBean.getDefaultRole();
		}
		
		if(roleBean == null){
			throw new NotFoundDefaultValidateServiceException("The default version processing service is not configured!");
		}
		
		return roleBean;
	}
	
	/**
	 * 校验是否传递/配置必须的参数
	 * @param loginSignFormBean
	 * @param channelInfo
	 * @param roleBean
	 * @throws NoSuchRequestParamException 
	 * @throws NoSuchConfigParamException 
	 */
	private void checkParam(LoginSignFormBean loginSignFormBean, Map<String, String> channelInfo, SignRuleBean roleBean) throws NoSuchRequestParamException, NoSuchConfigParamException{
		List<String> requestParams = roleBean.getRequestParamList();
		checkEmpty(loginSignFormBean, requestParams);
		
		List<String> configParams = roleBean.getConfigParamList();
		checkEmpty(channelInfo, configParams);
	}
	
	/**
	 * 校验是否传递必须的参数
	 * @param loginSignFormBean
	 * @param params
	 * @throws NoSuchRequestParamException 
	 */
	private void checkEmpty(LoginSignFormBean loginSignFormBean, List<String> params) throws NoSuchRequestParamException{
		Class<LoginSignFormBean> cls = LoginSignFormBean.class;
		StringBuilder sb = new StringBuilder();
		Field field = null;
		String paramName = null;
		Object value = null;
		JSONObject json = null;
		try {
			
			json = loginSignFormBean.getJsonData();
			
			for (String name : params) {
				//如果data里包含该参数，则跳过
				if(json != null && json.containsKey(name)){
					continue;
				}
				
				paramName = name;
				field = cls.getDeclaredField(name);
				field.setAccessible(true);
				value = field.get(loginSignFormBean);
				
				if(value == null || "".equals(value)){
					appendMsg(sb, name);
				}
			}
		} catch (NoSuchFieldException e) {
			throw new NoSuchRequestParamException(paramName + " does not exist in " + cls);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		if(sb.length() > 0){
			throw new NoSuchRequestParamException(sb.toString());
		}
	}
	
	/**
	 * 校验是否配置必须的参数
	 * @param map
	 * @param params
	 * @throws NoSuchRequestParamException 
	 */
	private void checkEmpty(Map<String, String> map, List<String> params) throws NoSuchConfigParamException{
		StringBuilder sb = new StringBuilder();
		for (String name : params) {
			if(!map.containsKey(name) || StringUtils.isBlank(map.get(name))){
				appendMsg(sb, name);
			}
		}
		if(sb.length() > 0){
			throw new NoSuchConfigParamException(sb.toString());
		}
	}

    /**
     * 获取到对应版本的参数，如果未获取到，则使用默认版本的参数
     * @param ruleBean 默认版本校验bean
     * @param loginSignFormBean 登录参数
     * @return
     */
	private Map<String, String> getChannelInfo(SignRuleBean ruleBean, LoginSignFormBean loginSignFormBean){
        Map<String, String> channelInfo = channelServiceTemplate
                .queryChannelInfo(loginSignFormBean.getChannelId(),
                		loginSignFormBean.getGameId(), loginSignFormBean.getVersion()/*传递的版本*/);
        //如果未获取到对应版本的参数，则使用默认版本的参数
        if (MapUtils.isEmpty(channelInfo)) {
            channelInfo = channelServiceTemplate.queryChannelInfo(loginSignFormBean.getChannelId(),
            		loginSignFormBean.getGameId(), ruleBean.getVersion()/*默认版本*/);
            //获取到默认版本参数后，视为对应版本的缓存数据
            if (MapUtils.isNotEmpty(channelInfo)) {
                loginTokenCacheOperationDao.writeChannelInfo(loginSignFormBean.getChannelId(),
                		loginSignFormBean.getGameId(), loginSignFormBean.getVersion(), channelInfo);
            }
        }
        return channelInfo;
    }

	/**
	 * 拼接错误信息
	 * @param sb
	 * @param name
	 */
	private void appendMsg(StringBuilder sb, String name){
		sb.append(String.format(MSG, name));
	}
}
