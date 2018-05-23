package com.linekong.login.auth.service.impl.qihu360;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;

import java.util.Map;

public class Qihu360ChannelValidateServiceImpl implements IVerifierRuleService {

    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        String token = jsonData.getString("token");
        String url = channelInfoMap.get("goUrl");

        String httpResult = channelServiceTemplate.getHttpsChannelService(url, new String[]{"access_token", "fields"}, new String[]{token, "id,name,avatar"}, false);
        if (!ChannelServiceTemplate.DEFAULT_HTTP_CONTENT.equals(httpResult)) {
            JSONObject resultJson = JSONObject.fromObject(httpResult);
            //如果没有返回用户信息，则认证失败
            if (resultJson.containsKey("id")) {
                loginFormBean.setUserId(resultJson.getString("id"));
                return true;
            } else {
                dataMap.put("error", resultJson.optString("error_code"));
                dataMap.put("msg", resultJson.optString("error"));
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        } else {
            //http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }
        return false;
    }
}
