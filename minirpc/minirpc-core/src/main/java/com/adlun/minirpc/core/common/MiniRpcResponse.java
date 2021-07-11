package com.adlun.minirpc.core.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcResponse implements Serializable {

    private Object responseData;

    private String message;

}
