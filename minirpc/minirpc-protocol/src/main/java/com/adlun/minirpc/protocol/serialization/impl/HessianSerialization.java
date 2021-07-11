package com.adlun.minirpc.protocol.serialization.impl;

import com.adlun.minirpc.protocol.serialization.IRpcSerialization;
import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class HessianSerialization implements IRpcSerialization {
    @Override
    public <T> byte[] serialize(T data) throws IOException {
        if (Objects.isNull(data)) {
            throw new NullPointerException();
        }
        byte[] results = null;
        HessianSerializerOutput hessianSerializerOutput;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(out);
            hessianSerializerOutput.writeObject(data);
            hessianSerializerOutput.flush();
            results = out.toByteArray();
        }
        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        if (Objects.isNull(data) || Objects.isNull(clz)) {
            throw new NullPointerException();
        }
        T result;

        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            HessianSerializerInput hessianSerializerInput = new HessianSerializerInput(in);
            result = (T) hessianSerializerInput.readObject(clz);
        }
        return result;
    }
}
