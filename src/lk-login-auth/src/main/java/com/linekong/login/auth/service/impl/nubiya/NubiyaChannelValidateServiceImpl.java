package com.linekong.login.auth.service.impl.nubiya;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NubiyaChannelValidateServiceImpl implements IVerifierRuleService {
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        String uid = jsonData.getString("uid");
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);//jsonData.getString("timestamp");
        String session_id = jsonData.getString("session_id");

        String appid = channelInfoMap.get("appid");
        String secretkey = channelInfoMap.get("secretkey");
        String goUrl = channelInfoMap.get("goUrl");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("data_timestamp", timestamp);
        paramMap.put("game_id", loginFormBean.getUserId());
        paramMap.put("session_id", session_id);
        String tmpSign =  channelServiceTemplate.getSignValueInfo(paramMap);
        String sign = md5Util.getMD5(tmpSign + ":" + appid + ":" + secretkey);
        //发送请求

        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("uid", uid));
        paramList.add(new BasicNameValuePair("data_timestamp", timestamp));
        paramList.add(new BasicNameValuePair("game_id", loginFormBean.getUserId()));
        paramList.add(new BasicNameValuePair("session_id", session_id));
        paramList.add(new BasicNameValuePair("sign", sign));

        String httpResult =  channelServiceTemplate.postHttpsChannelService(goUrl, ChannelServiceTemplate.DEFAULT_HEADERS, paramList);

        if (ChannelServiceTemplate.DEFAULT_HTTP_CONTENT.equals(httpResult)) {
            //http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        } else {
            JSONObject jsonResult = JSONObject.fromObject(httpResult);
            if (jsonResult.optInt("code") == 0) {
                return true;
            } else {
                dataMap.put("error", jsonResult.getString("code"));
                dataMap.put("msg", jsonResult.getString("message"));
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        }
        return false;
    }
}
