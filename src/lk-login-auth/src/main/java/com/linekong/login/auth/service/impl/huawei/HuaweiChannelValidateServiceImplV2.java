package com.linekong.login.auth.service.impl.huawei;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.RsaCryptoUtils;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import com.linekong.login.auth.utils.Base64;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


/**
 * 
 * @author kangyf
 * 华为登录认证
 *
 */
public class HuaweiChannelValidateServiceImplV2 implements IVerifierRuleService{

    private static final String METHOD = "external.hms.gs.checkPlayerSign";

	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Map<String, String> channelInfoMap,
			Map<String, Object> resultMap, LoginFormBean loginFormBean,
			ChannelServiceTemplate channelServiceTemplate) {

        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();
		
		/*
        1、构造源串
            源串的格式为a=urlencode(x)&b=urlencode(y)&c=urlencode(z)...
           （a）将除"cpSign"或"rtnSign"外的所有参数按key进行字典升序排列。
           （b）将（a）步中排序后的参数的value值分别进行urlencode，再将(key=value)用&拼接起来。
        2、生成签名值
           （a）将源串使用RSA算法（SHA256WithRSA）进行签名。
           （b）将签名后的字符串经过Base64编码，生成签名值。
           （c）对签名值urlencode。
		 */

        //客户端SDK返回的登陆令牌
        String playerSSign = new String(Base64.decode(jsonData.getString("gameAuthSign")));
        String playerLevel = jsonData.getString("playerLevel");
        String ts = jsonData.getString("ts");
        String playerId = loginFormBean.getUserId();
        //配置的参数
        String goUrl = channelInfoMap.get("goUrl");
        String appId = channelInfoMap.get("appid");
        String secretkey = channelInfoMap.get("secretkey");
        String cpId = channelInfoMap.get("cpid");
        String publicKey = channelInfoMap.get("publickey");


        Map<String, String> map = new HashMap<>();
        map.put("method", METHOD);
        map.put("appId", 	appId);
        map.put("cpId", 	cpId);
        map.put("ts", 	ts);
        map.put("playerId", 	playerId);
        map.put("playerLevel", 	playerLevel);
        map.put("playerSSign", 	playerSSign);

        String cpSign = RsaCryptoUtils.sign(format(map), secretkey, "utf-8");

        List<NameValuePair> valuePairs = parseParamMap(map);
        valuePairs.add(new BasicNameValuePair("cpSign", cpSign));

        String httpResult = channelServiceTemplate.postHttpsChannelService(goUrl, ChannelServiceTemplate.DEFAULT_HEADERS, valuePairs);

        //解析http请求返回值
        if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
            JSONObject jsonResult = JSONObject.fromObject(httpResult);
            //如果返回值有status,表示验证成功
            if(jsonResult.optInt("rtnCode") == 0){
                String data = "rtnCode=" + jsonResult.optString("rtnCode") + "&ts=" + jsonResult.optString("ts");
                if (RsaCryptoUtils.doCheck(data, jsonResult.optString("rtnSign"), publicKey, RsaCryptoUtils.SIGN_256_ALGORITHMS)) {
                    return true;
                } else {
                    dataMap.put("error", jsonResult.optInt("rtnCode"));
                    dataMap.put("msg", "第三方回传数据验证不通过");

                    resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
                }
            }else{
                dataMap.put("error", jsonResult.optInt("rtnCode"));
                dataMap.put("msg", jsonResult.optString("errMsg"));

                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        }else{	//http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }

        return false;
    }

    private static List<NameValuePair> parseParamMap(Map<String, String> map){
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            valuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return valuePairs;
    }

    /**
     * 根据参数Map构造排序好的参数串
     *
     * @param params
     * @return
     */
    private static String format(Map<String, String> params)
    {
        StringBuffer base = new StringBuffer();
        Map<String, String> tempMap = new TreeMap<String, String>(params);


        // 获取计算nsp_key的基础串
        try
        {
            for (Map.Entry<String, String> entry : tempMap.entrySet())
            {
                String k = entry.getKey();
                String v = entry.getValue();
                base.append(k).append("=").append(URLEncoder.encode(v, "UTF-8")).append("&");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println("Encode parameters failed.");
            e.printStackTrace();
        }


        String body = base.toString().substring(0, base.toString().length() - 1);
        // 空格和星号转义
        body = body.replaceAll("\\+", "%20").replaceAll("\\*", "%2A");

        return body;
    }
}
