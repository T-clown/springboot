
package com.springboot.common.spi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import static com.springboot.common.spi.CommonConstants.LOCALHOST_KEY;

/**
 * IP and Port Helper for RPC
 */
public class NetUtils {


    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    public static boolean isLocalHost(String host) {
        return host != null
                && (LOCAL_IP_PATTERN.matcher(host).matches()
                || host.equalsIgnoreCase(LOCALHOST_KEY));
    }


    /**
     * @param hostName
     * @return ip address or hostName if UnknownHostException
     */
    public static String getIpByHost(String hostName) {
        try {
            return InetAddress.getByName(hostName).getHostAddress();
        } catch (UnknownHostException e) {
            return hostName;
        }
    }


}
