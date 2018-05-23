package com.linekong.login.auth.service.impl.haodong;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/10
 * Time: 9:55
 */
public class HaodongChannelValidateServiceImpl implements IVerifierRuleService {
    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        //访问地址
        String url = channelInfoMap.get("goUrl");
        //开发者平台生成的secretkey
        String secretKey = channelInfoMap.get("secretkey");

        //SDK传递的用户名
        String userId = loginFormBean.getUserId();
        //SDK传递的token
        String token = jsonData.getString("token");
        //生成签名,规则userID=[userID]token=[token][appSecret]
        String sign = md5Util.getMD5("userID=" + userId + "token=" + token + secretKey);

        String[] paramNames = new String[]{"userID", "token", "sign"};
        String[] paramValues = new String[]{userId, token, sign};

        //http返回值
        String httpResult = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
        //解析http返回值
        if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
            JSONObject jsonResult = JSONObject.fromObject(httpResult);
            int resultCode = jsonResult.optInt("state");
            if (ChannelServiceTemplate.RET_CODE_1 == resultCode) {
                return true;
            } else {
                dataMap.put("error", resultCode);
                dataMap.put("msg", 	jsonResult.toString());
                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        } else {//http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }
        return false;
    }
}
