package com.linekong.login.auth.service.impl.qik;

import com.linekong.login.auth.service.ChannelServiceTemplate;
import com.linekong.login.auth.service.IVerifierRuleService;
import com.linekong.login.auth.utils.SysCodeConstant;
import com.linekong.login.auth.utils.md5Util;
import com.linekong.login.auth.web.formBean.LoginFormBean;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/12/18
 * Time: 10:29
 */
public class QikChannelValidateServiceImpl implements IVerifierRuleService {

    private static final String URL = "api.sy.7k7k.com/index.php";

    @Override
    public boolean validate(Map<String, String> channelInfoMap, Map<String, Object> resultMap, LoginFormBean loginFormBean, ChannelServiceTemplate channelServiceTemplate) {
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
        JSONObject jsonData = loginFormBean.getJsonData();

        //访问地址
        String url = channelInfoMap.get("goUrl");
        //客户端SDK返回的vkey
        String vkey = jsonData.getString("vkey");
        //客户端SDK返回的uid
        String uid = loginFormBean.getUserId();

        /*
         参数Key值加密规则:
         $url = api.sy.7k7k.com/index.php;
         $param = uid=xxx&vkey=xxx;
         $key = md5(md5($url).$param);
         */

        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", 		uid);
        map.put("vkey", 	vkey);
        String param = channelServiceTemplate.getSignValueInfo(map);

        String key = md5Util.getMD5(md5Util.getMD5(URL) + param);

        String[] paramNames = new String[]{"uid", "key", "vkey"};
        String[] paramValues = new String[]{uid, key, vkey};

        //发送http请求
        String httpResult = channelServiceTemplate.getChannelService(url, paramNames, paramValues, ChannelServiceTemplate.DEFAULT_HEADERS);
        //解析http请求返回值
        if(!httpResult.equals(ChannelServiceTemplate.DEFAULT_HTTP_CONTENT)){
            JSONObject resultJson = JSONObject.fromObject(httpResult);
            int ret = resultJson.getInt("status");
            /*
            * 结果说明
            *  status -2  :未登录
            *  status  0  :已登录
            **/
            if(ret == 0){
                return true;
            }else{	//如果返回值没有status,表示验证失败
                dataMap.put("error", ret);
                dataMap.put("msg", resultJson.optString("msg"));

                resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_CHANNEL);
            }
        }else{	//http请求错误
            resultMap.put(SysCodeConstant.RESULT_STATUS_STRING, SysCodeConstant.ERROR_HTTP);
        }

        return false;
    }
}
