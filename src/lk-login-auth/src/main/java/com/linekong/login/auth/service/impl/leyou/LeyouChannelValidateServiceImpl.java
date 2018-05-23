package com.linekong.login.auth.service.impl.leyou;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeyouChannelValidateServiceImpl implements IVerifierRuleService{
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        //http post请求参数
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("username", 	loginFormBean.getUserId()));//用户ID
        valuePairs.add(new BasicNameValuePair("memkey", jsonData.getString("token")));//token

        //http返回值
        String httpResult = channelServiceTemplate.postChannelService(channelInfoMap.get("goUrl"), ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);
        if (ChannelServiceTemplate.RET_STRING_SUCCESS.equals(httpResult)) {
            return true;
        } else {
            dataMap.put("error", httpResult);
            dataMap.put("msg", "乐游账号验证失败");
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
        }
        return false;
    }
}
