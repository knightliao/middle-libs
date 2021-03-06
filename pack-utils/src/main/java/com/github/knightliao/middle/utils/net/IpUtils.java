package com.github.knightliao.middle.utils.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/7 17:34
 */
@Slf4j
public class IpUtils {

    public static String getLocalIp() {

        try {

            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            List<String> ipv4Result = new ArrayList<>();
            List<String> ipv6Result = new ArrayList<>();

            while (enumeration.hasMoreElements()) {

                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration en = networkInterface.getInetAddresses();

                while (en.hasMoreElements()) {

                    InetAddress address = (InetAddress) en.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        } else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            if (!ipv4Result.isEmpty()) {
                Iterator var8 = ipv4Result.iterator();

                String ip;
                do {
                    if (!var8.hasNext()) {
                        return ipv4Result.get(ipv4Result.size() - 1);
                    }

                    ip = (String) var8.next();
                } while (ip.startsWith("127.0") || ip.startsWith("192.168"));

                return ip;

            } else if (!ipv6Result.isEmpty()) {

                return ipv6Result.get(0);

            } else {

                InetAddress localhost = InetAddress.getLocalHost();
                return normalizeHostAddress(localhost);
            }

        } catch (Exception ex) {

            log.error("Failed to obtain local address", ex);
            throw new RuntimeException(ex);
        }
    }

    private static String normalizeHostAddress(InetAddress localHost) {
        return localHost instanceof Inet6Address ? "[" + localHost.getHostAddress() + "]" : localHost.getHostAddress();
    }

    private static boolean validateIp(String ip) {
        return ip != null && ip.length() != 0 && !ip.equalsIgnoreCase("unknown");
    }

    /**
     * @return
     * @Description: ???????????????
     */
    public static String getHostName() throws Exception {

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();

            return hostname;

        } catch (UnknownHostException e) {

            throw new Exception();
        }
    }
}
