package com.linekong.login.auth.service.impl.songshu;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2018/1/9
 * Time: 18:00
 */
public class SongshuChannelValidateServiceImpl implements IVerifierRuleService {
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();
        //客户端传递的参数
        String token = jsonData.getString("token");
        //配置的参数
        String goUrl = channelInfoMap.get("goUrl");

        String httpResult = channelServiceTemplate.getChannelService(goUrl, new String[]{"token"}, new String[]{token}, ChannelServiceTemplate.DEFAULT_HEADERS);

        if (!ChannelServiceTemplate.DEFAULT_HTTP_CONTENT.equals(httpResult)) {
            System.out.println(httpResult);
            JSONObject resultJson = JSONObject.fromObject(httpResult);
            //如果没有返回用户信息，则认证失败
            if (SysCodeConstant.SUCCESS.equals(resultJson.optInt("code"))) {
                loginFormBean.setUserId(resultJson.getJSONObject("data").getString("userId"));
                return true;
            } else {
                dataMap.put("error", resultJson.optInt("code"));
                dataMap.put("msg", resultJson.optString("msg"));
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        } else {
            //http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }

        return false;
    }

}
