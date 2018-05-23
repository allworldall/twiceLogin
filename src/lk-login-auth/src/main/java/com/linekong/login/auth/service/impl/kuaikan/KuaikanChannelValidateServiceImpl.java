package com.linekong.login.auth.service.impl.kuaikan;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

public class KuaikanChannelValidateServiceImpl implements IVerifierRuleService {
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();
        //客户端SDK返回的登陆令牌
        String accessToken = jsonData.optString("token");

        String appid = channelInfoMap.get("appid");
        String secretkey = channelInfoMap.get("secretkey");
        String goUrl = channelInfoMap.get("goUrl");
        String openId = loginFormBean.getUserId();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_id", appid);
        paramMap.put("open_id", openId);
        paramMap.put("access_token", accessToken);

        //获取sign
        String stringSignTemp = channelServiceTemplate.getSignValueInfo(paramMap).concat("&key=").concat(secretkey);
        String sign = Base64.encode(md5Util.md5(stringSignTemp));//签名

        String[] paramNames = new String[]{
                "app_id", "open_id", "access_token", "sign"
        };
        String[] paramValues = new String[]{
                appid, openId, accessToken, sign
        };
        //获取接口返回结果
        String result = channelServiceTemplate.getChannelService(goUrl, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);

        if (ChannelServiceTemplate.DEFAULT_HTTP_CONTENT.equals(result)) {
            //http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        } else {
            JSONObject jsonResult = JSONObject.fromObject(result);
            //code==200时
            if (jsonResult.optInt("code") == ChannelServiceTemplate.RET_CODE_200) {
                //ret==true时验证成功
                if (jsonResult.getJSONObject("data").optBoolean("ret")) {
                    dataMap.put("access_token", accessToken);
                    return true;
                }
            }
            dataMap.put("error", jsonResult.getString("code"));
            dataMap.put("msg", jsonResult.getString("msg"));
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
        }
        return false;
    }
}
