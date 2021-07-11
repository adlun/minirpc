package com.adlun.minirpc.core.common;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceMeta {

    private String serviceName;

    private String serviceVersion;

    private String group;

    private String ipAddr;

    private int port;

    private boolean enabled;

}
