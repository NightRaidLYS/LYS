package com.lys.sys.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class ClientHostAddress {
    /**
     * 获取请求客户端IP地址
     *
     * @param request Http请求对象
     * @return 请求客户端IP地址
     */
    public static String getClientHostAddress(HttpServletRequest request)
    {
        String ipSrv = request.getHeader("X-Real-IP");
        String ipAddr = request.getHeader("X-Forwarded-For");
        if (! StringUtils.isBlank(ipAddr))
        {
            List<String> strings = Arrays.asList(ipAddr.split(",")).stream().map(item -> item.trim()).collect(Collectors.toList());
            int index= strings.indexOf(ipSrv);
            if(index>=1){
                ipAddr= strings.get(index-1);
            }
        }

        if (StringUtils.isBlank(ipAddr) || "unknown".equalsIgnoreCase(ipAddr))
        {
            ipAddr = request.getRemoteAddr();
        }
//        if ("127.0.0.1".equals(ipAddr))
        //if ("127.0.0.1".equals(ipAddr) || "0:0:0:0:0:0:0:1".equals(ipAddr))
        String finalIpAddr=ipAddr;
        if (Stream.of("0:0:0:0:0:0:0:1","127.0.0.1").anyMatch(item->finalIpAddr.equals(item)))
        {
            InetAddress inet = null;
            try
            { // 根据网卡取本机配置的IP
                inet = InetAddress.getLocalHost();
                ipAddr = inet.getHostAddress();
            }
            catch (UnknownHostException e)
            {
                //LOGGER.error("Get Local host inet Address error!", e);
                //e.printStackTrace(e);
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddr != null && ipAddr.length() > 15)
        {
            if (ipAddr.indexOf(",") > 0)
            {
                ipAddr = ipAddr.substring(0, ipAddr.indexOf(","));
            }
        }
        return ipAddr;
    }

}
