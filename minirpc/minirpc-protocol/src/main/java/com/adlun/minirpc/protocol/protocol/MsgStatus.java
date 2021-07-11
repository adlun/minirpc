package com.adlun.minirpc.protocol.protocol;

import lombok.Getter;

public enum MsgStatus {
    SUCCESS((byte) 0),

    FAILED((byte) 1);

    @Getter
    private byte code;

    MsgStatus(byte code) {
        this.code = code;
    }

}
