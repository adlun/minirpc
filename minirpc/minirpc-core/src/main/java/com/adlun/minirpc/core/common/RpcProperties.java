package com.adlun.minirpc.core.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    private String registryAddr;

    private int servicePort;

    private String registryType;

}
