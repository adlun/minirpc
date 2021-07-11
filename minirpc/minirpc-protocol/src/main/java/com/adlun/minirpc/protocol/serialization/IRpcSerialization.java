package com.adlun.minirpc.protocol.serialization;

import java.io.IOException;

public interface IRpcSerialization {
    <T> byte[] serialize(T data) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}
