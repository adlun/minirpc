package com.adlun.minirpc.protocol.protocol;

import lombok.Getter;

public enum MsgType {

    REQUEST((byte) 0),

    RESPONSE((byte) 1),

    HEARTBEAT((byte) 2);

    @Getter
    private byte type;

    MsgType(byte type) {
        this.type = type;
    }

    public static MsgType findByType(byte msgType) {
        MsgType[] msgTypes = MsgType.values();
        for (MsgType type : msgTypes) {
            if (type.getType() == msgType) {
                return type;
            }
        }
        return null;
    }
}
