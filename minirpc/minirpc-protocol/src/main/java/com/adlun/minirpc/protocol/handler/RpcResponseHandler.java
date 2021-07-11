package com.adlun.minirpc.protocol.handler;

import com.adlun.minirpc.core.common.MiniRpcFuture;
import com.adlun.minirpc.core.common.MiniRpcRequestHolder;
import com.adlun.minirpc.core.common.MiniRpcResponse;
import com.adlun.minirpc.protocol.protocol.MiniRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcResponseHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcResponse> msg) {
        long requestId = msg.getHeader().getMsgId();
        MiniRpcFuture<MiniRpcResponse> future = MiniRpcRequestHolder.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }
}

