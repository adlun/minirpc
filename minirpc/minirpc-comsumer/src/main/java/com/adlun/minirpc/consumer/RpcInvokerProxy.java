package com.adlun.minirpc.consumer;

import com.adlun.minirpc.core.common.MiniRpcFuture;
import com.adlun.minirpc.core.common.MiniRpcRequest;
import com.adlun.minirpc.core.common.MiniRpcRequestHolder;
import com.adlun.minirpc.core.common.MiniRpcResponse;
import com.adlun.minirpc.protocol.protocol.MiniRpcProtocol;
import com.adlun.minirpc.protocol.protocol.MsgHeader;
import com.adlun.minirpc.protocol.protocol.MsgType;
import com.adlun.minirpc.protocol.protocol.ProtocolConstants;
import com.adlun.minirpc.registry.service.IRegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class RpcInvokerProxy implements InvocationHandler {

    private final String serviceGroup;
    private final String serviceVersion;
    private final long timeout;
    private final IRegistryService registryService;

    public RpcInvokerProxy(String serviceGroup, String serviceVersion, long timeout, IRegistryService registryService) {
        this.serviceGroup = serviceGroup;
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
        MsgHeader header = new MsgHeader();
        long requestId = MiniRpcRequestHolder.generateRequestId();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setMsgId(requestId);
        header.setSerializationAlgorithm((byte) 0x0);
        header.setMsgType(MsgType.REQUEST.getType());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);

        MiniRpcRequest request = new MiniRpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setServiceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setServiceGroup(this.serviceGroup);
        request.setParams(args);
        protocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        MiniRpcFuture<MiniRpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        MiniRpcRequestHolder.put(requestId, future);
        rpcConsumer.sendRequest(protocol, this.registryService);

        // TODO hold request by ThreadLocal


        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getResponseData();
    }
}
