package com.github.knightliao.middle.web.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liaoqiqi
 * @version 2014-4-23
 */
public class WebIpUtils {

    public static String getIp(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (!validateIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (!validateIp(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (!validateIp(ip)) {
                    ip = request.getRemoteAddr();
                }
            }
        }
        return ip;
    }

    private static boolean validateIp(String ip) {
        return ip != null && ip.length() != 0 && !ip.equalsIgnoreCase("unknown");
    }

    /**
     * 获取连接WEB服务器的真实IP，前端有UTR的拦截请求
     */
    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");

        } else {
            String[] ipArray = ip.split("\\,");
            ip = ipArray[0];//  获取第一个IP
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("clientip");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
