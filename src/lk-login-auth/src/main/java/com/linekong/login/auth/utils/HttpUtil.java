package com.linekong.login.auth.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.linekong.login.auth.utils.log.LoggerUtil;


public class HttpUtil {
	
	private static final	String  DEFAULT_HTTP_CONTENT 		= "default";	//http请求错误默认值

    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(5000).build();


    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean downloadFile(String url, String path) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            File targetFile = new File(path);
            //创建文件夹
            File directory = targetFile.getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(DEFAULT_REQUEST_CONFIG);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            try {
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
                    httpEntity.writeTo(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    return true;
                } else {
                    httpGet.abort();
                }
            } finally {
                httpResponse.close();
            }
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return false;
    }

    /**
     * @param primStr
     * @return
     * @Description 使用gzip进行压缩
     */
    public static String compressByGZIP(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return DatatypeConverter.printBase64Binary(out.toByteArray());
    }

    /**
     * @param compressedStr
     * @return
     * @Description 使用gzip进行解压缩
     */
    public static String uncompressByGZIP(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = DatatypeConverter.parseBase64Binary(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return decompressed;
    }



    public static String doGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(DEFAULT_REQUEST_CONFIG);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpGet.abort();
            }
        } catch (Exception ex) {
            LoggerUtil.error(HttpUtil.class, " URL = " + url +", " + ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }

    public static String doGetWithResultContent(String url, String[] paramNames, String[] paramValues, Header[] headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder encodeUrl = new StringBuilder(url);
        try {
            for(int i=0; i<paramNames.length; i++) {
                if(i == 0) {
                    encodeUrl.append("?");
                } else {
                    encodeUrl.append("&");
                }
                encodeUrl.append(URLEncoder.encode(paramNames[i], "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(paramValues[i], "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.error(HttpUtil.class, "doGet error 1 url=" + encodeUrl.toString() + ", msg=" + e.getMessage());
            return DEFAULT_HTTP_CONTENT; 
        }
        HttpGet httpGet = new HttpGet(encodeUrl.toString());
        //设置headers
        if(headers != null){
        	httpGet.setHeaders(headers);
        }
        httpGet.setConfig(DEFAULT_REQUEST_CONFIG);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                httpGet.abort();
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            String resultStr = EntityUtils.toString(httpEntity, "UTF-8");
            return resultStr;
        } catch (Exception ex) {
        	LoggerUtil.error(HttpUtil.class, "doGet error 2 url=" + encodeUrl.toString() + ", msg=" + ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }


    public static String doPostWithHeader(String url, Header header, List<NameValuePair> valuePairs) {
        return doPostWithHeaders(url, new Header[]{header}, valuePairs);
    }

    public static String doPostWithHeaders(String url, Header[] headers, List<NameValuePair> valuePairs)
    {
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
        } catch (Exception ex)
        {
        	LoggerUtil.error(HttpUtil.class, "doPostWithHeaders error: "+ ex.getMessage());
        }
        return doPostWithHeadersAndEntity(url, headers, entity);
    }
    
    public static String doPostWithHeaders(String url, Header[] headers, String jsonData)
    {
        HttpEntity entity = null;
        try {
            entity = new StringEntity(jsonData, "UTF-8");
        } catch (Exception ex)
        {
        	LoggerUtil.error(HttpUtil.class, "doPostWithHeaders error: "+ ex.getMessage());
        }
        return doPostWithHeadersAndEntity(url, headers, entity);
    }

    public static String doPostWithHeadersAndEntity(String url, Header[] headers, HttpEntity entity)
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setHeaders(headers);
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(5000).build(); // 设置超时
            httpPost.setConfig(requestConfig);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpPost.abort();
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (Exception ex) {
        	LoggerUtil.error(HttpUtil.class, "doPostWithHost error url=" + url + " headers=" + headers.length + " values=" + entity.toString()+ " msg=" + ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }

    public static String doPutWithAuthAndHeadersAndEntity(String url, Header[] headers, String userName, String password, HttpEntity entity)
    {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpPut.setConfig(requestConfig);
        try {
            httpPut.setHeaders(headers);
            httpPut.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPut);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpPut.abort();
            }
        } catch (Exception ex) {
        	LoggerUtil.error(HttpUtil.class, "doPutWithAuthAndHeadersAndEntity error url=" + url + " headers=" + headers.length + " values=" + entity.toString() + " msg=" + ex.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }

    public static String doPostWithAuthAndHeadersAndEntity(String url, Header[] headers, String userName, String password, HttpEntity entity)
    {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeaders(headers);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpPost.abort();
            }
        } catch (Exception ex) {
        	LoggerUtil.error(HttpUtil.class, "doPostWithAuthAndHeadersAndEntity error url=" + url + " headers=" + headers.length + " values=" + entity.toString() + ", error=" + ex);
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }

    public static String doDeleteWithAuthAndHeaders(String url, Header[] headers, String userName, String password)
    {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpDelete.setConfig(requestConfig);
        try {
            httpDelete.setHeaders(headers);
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpDelete.abort();
            }
        } catch (Exception ex) {
        	LoggerUtil.error(HttpUtil.class, "doDeleteWithAuthAndHeaders error url=" + url + " headers=" + headers.length + ", error=" + ex);
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
        }
        return DEFAULT_HTTP_CONTENT;
    }
    
    //发送https的get请求
    @SuppressWarnings("resource")
	public static String doGetHttpsWithResultContent(String url, String[] paramNames, String[] paramValues, boolean isEncode) {
        
        try {
        	HttpClient httpClient = new SSLClient();
            StringBuilder encodeUrl = new StringBuilder(url);
            
            for(int i=0; i<paramNames.length; i++) {
                if(i == 0) {
                    encodeUrl.append("?");
                } else {
                    encodeUrl.append("&");
                }
                if(isEncode){
                	encodeUrl.append(URLEncoder.encode(paramNames[i], "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(paramValues[i], "UTF-8"));
                }else{
	                encodeUrl.append(paramNames[i])
	                .append("=")
	                .append(paramValues[i]);
                }
                

                
            }
            
            HttpGet httpGet = new HttpGet(encodeUrl.toString());
            httpGet.setConfig(DEFAULT_REQUEST_CONFIG);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                httpGet.abort();
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            String resultStr = EntityUtils.toString(httpEntity, "UTF-8");
            return resultStr;
        } catch (Exception e) {
            LoggerUtil.error(HttpUtil.class , e.getMessage());
            return DEFAULT_HTTP_CONTENT;
        }
    }
    
    //发送https请求，数据为json数据
    public static String doHttpsPostWithHeaders(String url, Header[] headers, List<NameValuePair> valuePairs)
    {
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(valuePairs, "UTF-8");
        } catch (Exception ex)
        {
            LoggerUtil.error(HttpUtil.class, "doHttpsPostWithHeaders error: " + ex.getMessage());
        }
        return doHttpsPostWithHeadersAndEntity(url, headers, entity);
    }
    
    
  //发送https请求，数据为键值对
    public static String doHttpsPostWithHeaders(String url, Header[] headers, String jsonData)
    {
        HttpEntity entity = null;
        try {
            entity = new StringEntity(jsonData, "UTF-8");
        } catch (Exception ex)
        {
            LoggerUtil.error(HttpUtil.class, "doHttpsPostWithHeaders error: " + ex.getMessage());
        }
        return doHttpsPostWithHeadersAndEntity(url, headers, entity);
    }
    
    @SuppressWarnings("resource")
	public static String doHttpsPostWithHeadersAndEntity(String url, Header[] headers, HttpEntity entity)
    {
    	
        try {
        	HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeaders(headers);
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(5000).build(); // 设置超时
            httpPost.setConfig(requestConfig);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            } else {
                httpPost.abort();
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (Exception ex) {
            LoggerUtil.error(HttpUtil.class, "doHttpsPostWithHost error url=" + url + " headers=" + headers.length + " values=" + entity.toString()+ ex);
        } 
        return DEFAULT_HTTP_CONTENT;
    }

    /**
     * 如果转化是被，会变为null
     *
     * @param str
     */
    public static Integer convertToInteger(String str) {
        if (str == null)
            return null;
        try {
            Integer result = Integer.parseInt(str);
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 如果返回null 证明转换失败
     *
     * @param str
     * @param dateFormat
     * @return
     */
    public static Date convertToDate(String str, SimpleDateFormat dateFormat) {
        if (str == null) {
            return null;
        }
        try {
            Date date = dateFormat.parse(str);
            return date;
        } catch (Exception ex) {
            return null;
        }

    }


    public static boolean isEeq(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            if (s2 == null) {
                return false;
            } else {
                return s1.equals(s2);
            }
        }
    }

}
