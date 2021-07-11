package com.adlun.minirpc.protocol.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class MiniRpcProtocol<T> implements Serializable {

    private MsgHeader header;
    private T body;
}
