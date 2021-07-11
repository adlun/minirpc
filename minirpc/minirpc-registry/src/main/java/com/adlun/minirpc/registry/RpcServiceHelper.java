package com.adlun.minirpc.registry;

import org.apache.commons.lang3.StringUtils;

public class RpcServiceHelper {

    public static String buildServiceKey(String serviceName, String version, String group) {
        return StringUtils.joinWith("#", serviceName, version, group);
    }
}
