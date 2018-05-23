package com.linekong.login.auth.service.impl.huashenghuyu;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.HttpUtil;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;

public class HuaShengHuYuChannelValidateServiceImpl implements IVerifierRuleService{

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {

		try {
			//http://userver.huashy.cn:8080/user/verifyAccount
			String goUrl = channelInfoMap.get("goUrl");//请求地址
			String appKey = channelInfoMap.get("appkey");//请求地址
			String userId = loginFormBean.getUserId();//用户ID
			JSONObject jsonData = loginFormBean.getJsonData();
			//客户端传过来的token
			String token = jsonData.getString("token");
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");

			//Md5 签名值（32 位小写）md5("userID="+userID+"token="+token+appKey)
			String sign = md5Util.getMD5("userID="+userId+"token="+token+appKey);
			String requestURL = goUrl+"?userID="+userId+"&token="+token+"&sign="+sign;
			String httpResult =  HttpUtil.doGet(requestURL);
			if(StringUtils.isNotBlank(httpResult)){
				if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
					JSONObject resultJson = JSONObject.fromObject(httpResult);
					if(resultJson.optInt("state") == channelServiceTemplate.RET_CODE_1){
						String data = resultJson.optString("data");
						JSONObject dataJson = JSONObject.fromObject(data);
						loginFormBean.setUserId(dataJson.optString("userID"));
						return true;
					} else {
						dataMap.put("error", resultJson.optInt("state"));
						dataMap.put("msg", getErrorMessage(resultJson.optString("state")));
						resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
					}
				} else {
					resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
				}
			} else {
				resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
		}
		return false;
	}

	private String getErrorMessage(String state){
		String resultMsg = "";
		switch (state) {
		case "1":
			resultMsg = "成功";
		case "2":
			resultMsg = "游戏不存在 ";
			break;
		case "3":
			resultMsg = "渠道不存在";
		case "6":
			resultMsg = "userId 不存在";
			break;
		case "9":
			resultMsg = "Token 验证失败";
		case "10":
			resultMsg = "金额失败小于 0";
		case "11":
			resultMsg = "参数类型出错 ";
		case "12":
			resultMsg = "签名失败";
			break;
		case "13":
			resultMsg = "充值未开放";
			break;
		default:
			resultMsg = "未知";
			break;
		}
		return resultMsg;
	}
}
