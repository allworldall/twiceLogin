package com.linekong.login.auth.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.linekong.login.auth.service.LoginTokenValidateService;
import com.linekong.login.auth.service.ValidateAppIdService;
import com.linekong.login.auth.utils.JSONUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.log.LoggerUtil;
import com.linekong.login.auth.utils.validate.support.ValidateService;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import com.linekong.login.auth.web.formBean.LoginSignFormBean;
import com.linekong.login.auth.web.formBean.ValidateTokenFormBean;
import com.linekong.login.auth.web.service.ClientFacade;


@Controller("/validateToken")
public class LoginTokenValidateController {

	@Autowired
	private ClientFacade clientFacade;
	@Resource
	private LoginTokenValidateService loginTokenValidateService;
	@Resource
	private ValidateAppIdService validateAppIdService;
	
	@RequestMapping(value = "/portal", method = RequestMethod.POST)
	public void sdkValidateToken(LoginFormBean loginFormBean, HttpServletResponse response){
		
		try {
			String logStr = "portal request:"+loginFormBean.toString();
			//验证是否传递必传参数
			ValidateService.valid(loginFormBean);
			Map<String, Object> retMap = new HashMap<String, Object>();
			if(!loginFormBean.validateSign()){//验证数据有效性
				retMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_VALIDATE_PARAM);
				retMap.put("data", null);
			}else{
				retMap = clientFacade.doClient(loginFormBean, loginFormBean.getChannelId());
			}
			String strResult = JSONUtil.toString(retMap);
			logStr = logStr + " result:"+strResult;
			LoggerUtil.info(LoginTokenValidateController.class, logStr);
			response(response, strResult);
		} catch (Exception e) {
			LoggerUtil.error(LoginTokenValidateController.class, "portal exception:"+loginFormBean.toString(), e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR);
			retMap.put("data", null);
			response(response, JSONUtil.toString(retMap));
		}
	}
	
	@RequestMapping("/validate")
	public void serverValidateToken(ValidateTokenFormBean validateTokenFormBean, HttpServletResponse response){
		try {
			String logStr = "validate request:"+validateTokenFormBean.toString();
			ValidateService.valid(validateTokenFormBean);
			Integer result = loginTokenValidateService.validateToken(validateTokenFormBean);
			if(result == SysCodeConstant.SUCCESS){
				loginTokenValidateService.updateLoginTokenInfo(validateTokenFormBean);
			}
			
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put(SysCodeConstant.RESULT_STATUS_STRING, result);
			retMap.put("data", null);
			String strResult = JSONUtil.toString(retMap);
			logStr = logStr + " result:"+strResult;
			LoggerUtil.info(LoginTokenValidateController.class, logStr);
			response(response, strResult);
		} catch (Exception e) {
			LoggerUtil.error(LoginTokenValidateController.class, "validate exception:"+validateTokenFormBean.toString(),e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR);
			retMap.put("data", null);
			response(response, JSONUtil.toString(retMap));
		}
	}
	
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public void sdkSign(LoginSignFormBean loginSignFormBean, HttpServletResponse response){
		try {
			String logStr = "sign request:"+loginSignFormBean.toString();
			//验证是否传递必传参数
			ValidateService.valid(loginSignFormBean);
			Map<String, Object> retMap = new HashMap<String, Object>();
			if(!loginSignFormBean.validateSign()){//验证数据有效性
				retMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_VALIDATE_PARAM);
				retMap.put("data", null);
			}else{
				retMap = clientFacade.doClient(loginSignFormBean, loginSignFormBean.getChannelId());
			}
			String strResult = JSONUtil.toString(retMap);
			logStr = logStr + " result:"+strResult;
			LoggerUtil.info(LoginTokenValidateController.class, logStr);
			response(response, strResult);
		} catch (Exception e) {
			LoggerUtil.error(LoginTokenValidateController.class, "sign exception:"+loginSignFormBean.toString(), e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR);
			retMap.put("data", null);
			response(response, JSONUtil.toString(retMap));
		}
	}
	

	public void response(HttpServletResponse response, final String content) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.write(content);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally{
			try {
				if(out != null){out.close();}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
