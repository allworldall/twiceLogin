package com.linekong.login.auth.service;

import com.linekong.login.auth.task.IDNSTask;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: maxuan
 * Date: 2017/11/24
 * Time: 13:51
 * 将host信息提供给第三方服务器，解决ip访问被拒绝的情况
 */
@Aspect
@Component
public class HttpServiceProxy {

    @Autowired
    private IDNSTask idnsTask;

    @Pointcut("execution(* *com.linekong.login.auth.service.ChannelServiceTemplate.*ChannelService(..))")
    private void callMethod(){}

    @Around("callMethod()")
    public Object doCallMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object res = null;
        Header[] useHeader = null;
        Object[] args = pjp.getArgs();
        String url = args[0].toString();
        //获取响应最快的url
        String fastUrl = idnsTask.getFastIp(url);
        //获取url对应的域名
        String host = idnsTask.getHostMap().get(fastUrl);
        if (StringUtils.isNotBlank(host)){
            args[0] = fastUrl;
            for (int i = 0, len = args.length; i < len; i++) {
                if (args[i] instanceof Header[]) {
                    useHeader = (Header[]) args[i];
                    useHeader = Arrays.copyOf(useHeader, useHeader.length + 1);
                    useHeader[useHeader.length - 1] = new BasicHeader("Host", host);
                    args[i] = useHeader;
                }
            }
        }

        long start = System.currentTimeMillis();
        res = pjp.proceed(args);
        long end = System.currentTimeMillis();

        //根据响应时间决定是否重置最快ip
        idnsTask.resetIp(url, end - start);

        return res;
    }
}
