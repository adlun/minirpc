package com.adlun.minirpc.core.common;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MiniRpcRequestHolder {

    private static AtomicLong REQUEST_ID = new AtomicLong(0);

    private static Map<Long, MiniRpcFuture<MiniRpcResponse>> holder = new ConcurrentHashMap<>();

    public static Long generateRequestId() {
        return REQUEST_ID.incrementAndGet();
    }

    public static void put(Long requestId, MiniRpcFuture<MiniRpcResponse> future){
        holder.put(requestId, future);
    }

    public static MiniRpcFuture<MiniRpcResponse> getFuture(Long requestId) {
        if (Objects.isNull(requestId)) {
            return null;
        }
        return holder.get(requestId);
    }

    public static MiniRpcFuture<MiniRpcResponse> remove(Long requestId) {
        return holder.remove(requestId);
    }
}
