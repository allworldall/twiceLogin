package com.linekong.login.auth.service.impl.caohua;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.Base64;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/30
 * Time: 10:36
 */
public class CHAChannelValidateServiceImpl implements IVerifierRuleService{
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        //获取token
        String token = new String(Base64.decode(jsonData.getString("token")));
        //草花用户 ID
        String userid = loginFormBean.getUserId();
        //访问地址
        String url = channelInfoMap.get("goUrl");
        //cpId
        String appid = channelInfoMap.get("appid");
        //appKey
        String appKey = channelInfoMap.get("appkey");

        long times = System.currentTimeMillis() / 1000;

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("token", token);
        paramMap.put("userid", userid);
        paramMap.put("times", String.valueOf(times));

        String sign = md5Util.getMD5(channelServiceTemplate.getSignValueInfo(paramMap) + appKey);
        sign = sign.toUpperCase();

        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("appid", appid));//appkey
        valuePairs.add(new BasicNameValuePair("token", token));//token
        valuePairs.add(new BasicNameValuePair("userid", userid));//userid
        valuePairs.add(new BasicNameValuePair("times", String.valueOf(times)));//times
        valuePairs.add(new BasicNameValuePair("sign", sign));//sign

        //http返回值
        String httpResult = channelServiceTemplate.postChannelService(url, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);

        if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
            JSONObject jsonResult = JSONObject.fromObject(httpResult);
            if (jsonResult.optInt("code") == ChannelServiceTemplate.RET_CODE_200) {
                return true;
            }

            dataMap.put("error", jsonResult.optString("code"));
            dataMap.put("msg", jsonResult.optString("msg"));
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);

        } else { //http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }
        return false;
    }
}
