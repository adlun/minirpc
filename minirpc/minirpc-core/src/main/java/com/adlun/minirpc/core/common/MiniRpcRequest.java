package com.adlun.minirpc.core.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcRequest implements Serializable {

    private static final long serialVersionUID = 6695383790847736893L;

    private String serviceName;

    private String methodName;

    private String serviceVersion;

    private String servicePort;

    private String serviceGroup;

    private Object[] params;

    private Class[] paramTypes;
}
