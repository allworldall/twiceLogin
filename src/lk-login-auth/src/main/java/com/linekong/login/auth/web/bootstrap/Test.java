package com.linekong.login.auth.web.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linekong.login.auth.utils.*;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws Exception {
//        Gson gson = new Gson();
//        JSONObject json = new JSONObject();
//        HashMap<String, Object> requestMap = new HashMap<String, Object>();
//        json.put("uid", 123);
//        json.put("gameId", 456);
//        System.out.println(json);
        for (int i = 0; i < 100; i ++) {
            Entry entry = new Entry();
            entry.setKey("aabbcc" + (i % 2 == 0 ? "00" : "xx"));
            new TestThread(entry).start();
        }
    }

	public static String getSignValueInfo(Map<String, String> map){
		Map<String, String> comparatorMap = new TreeMap<String, String>(
		        new Comparator<String>() {
		            public int compare(String obj1, String obj2) {
		                // 降序排序
		                return obj1.compareTo(obj2);
		            }
	        });
		comparatorMap.putAll(map);
		String result = "";
		for(Map.Entry<String, String> entry : comparatorMap.entrySet()){
			result += entry.getKey() + "=" + entry.getValue() + "&";
		}
		if(result != null){
			result = result.substring(0, result.length() - 1);
		}
		return result;
		
	}	

	private static void xiaomingMain()  throws Exception {
        String encodestr = URLEncoder.encode("aaa dddd", "UTF-8");
        System.out.println("encodestr:"+encodestr);

        String accessToken = URLEncoder.encode("aaa dddd", "UTF-8").replaceAll("\\+", "%2B");
        System.out.println("accessToken:"+accessToken);

        String channelInfo = "{\"cpId\":100,\"gameId\":12345,\"channelId\":\"2\",\"serverId\":0}";
        Map<String, Object> channelInfoMap = JSONUtil.objFromJsonString(channelInfo, Map.class);
        double value = (double)channelInfoMap.get("cpId");
        int cpId = (int)value;
        System.out.println("cpId="+cpId);

        HashMap<String, Object> tmpMap = new HashMap<String, Object>();
        tmpMap.put("sid", "xieadjkfjsiql");
        String str = JSONUtil.objToJsonString(tmpMap);
        HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", System.currentTimeMillis());
        resultMap.put("data", tmpMap);
        resultMap.put("hh", 896);
        resultMap.put("abd", "abcd");
        System.out.println("str:"+JSONUtil.objToJsonString(resultMap));


        HashMap<String, String> infoMap = new HashMap<String, String>();
        infoMap.put("id", "9996");
        infoMap.put("data", "sddsf");
        infoMap.put("hh", "aaa");
        infoMap.put("abd", "abcd");
        System.out.println("info:"+getSignValueInfo(infoMap));

        String httpResult= "{"+
                "\"id\":1330395097,"+
                "\"state\":{\"code\":0, \"msg\":\"会话刷新成功\"},"+
                "\"data\":{\"suid\":123456, \"nickName\":\"张三\"}"+
                "}";
        System.out.println("httpResult:"+httpResult);
        Map<String, Object> httpMap = (Map<String, Object>)JSONUtil.objFromJsonString(httpResult, Map.class);
        Map<String, Object> stateMap = (Map<String, Object>)httpMap.get("state");
        double code = (Double)stateMap.get("code");
        System.out.println("code="+(int)code);
        String uuid = UUIDUtil.getUuid();
        System.out.println("uuid:"+uuid+",len="+uuid.length());

        String sign = Base64.encode(md5Util.md5("{\"command\":\"add\"}1234567890"));
        System.out.println("sign:"+sign);

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("http://localhost:8080/lk-login-auth/portal/?");
            Map<String, Object> tokenMap = new HashMap<String, Object>();
            tokenMap.put("token", "TOKEN_J7jVANN4IJWEmjMDbqf3ccd6XXcS46EMextWY8jc98D3welKRONisQ==");
            tokenMap.put("ssoid", "118880159");
            String dataValue = JSONUtil.objToJsonString(tokenMap);
            sb.append("data="+URLEncoder.encode(dataValue, "utf-8"));
//			sb.append("data=aaa");
            sb.append("&channelId=4&userId=118880159&gameId=2043&version=2.0.0");
            String reqUrl = sb.toString();
            System.out.println("requestUrl:"+reqUrl);

            URL url = new URL(reqUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.connect();
            int retCode = con.getResponseCode();
            System.out.println("retCode:"+retCode);
            if (retCode == 200){
                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = reader.readLine())!= null){
                    System.out.println(line);
                }
            }
            Thread.sleep(1000);


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static class TestThread extends Thread{
        Entry entry;

        public TestThread(Entry entry) {
            this.entry = entry;
        }

        @Override
        public void run() {
//            System.out.print("被加锁的key --- > " + entry.key + "    ");
            entry.out();
        }
    }
    static class Entry {
	    private String key;
        private static int num;
        private static LockUtil lockUtil = new LockUtil();


        public void setKey(String key) {
            this.key = key;
        }

        public void out(){
            try {
                lockUtil.lock(key);
                num = num + 1;
                System.out.println(key + " ------> " + num);
                num = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockUtil.release(key);
            }

//            synchronized (key) {
//                num = num + 1;
//                System.out.println(key + " ------> " + num);
//                num = 0;
//            }
        }
    }
}
