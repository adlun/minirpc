package com.adlun.minirpc.consumer;

import com.adlun.minirpc.core.common.MiniRpcRequest;
import com.adlun.minirpc.core.common.ServiceMeta;
import com.adlun.minirpc.protocol.codec.MiniRpcDecoder;
import com.adlun.minirpc.protocol.codec.MiniRpcEncoder;
import com.adlun.minirpc.protocol.handler.RpcResponseHandler;
import com.adlun.minirpc.protocol.protocol.MiniRpcProtocol;
import com.adlun.minirpc.registry.RpcServiceHelper;
import com.adlun.minirpc.registry.service.IRegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MiniRpcEncoder())
                                .addLast(new MiniRpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(MiniRpcProtocol<MiniRpcRequest> protocol, IRegistryService registryService) throws Exception {
        MiniRpcRequest request = protocol.getBody();
        Object[] params = request.getParams();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getServiceName(), request.getServiceVersion(), request.getServiceGroup());

        ServiceMeta serviceMetadata = registryService.discovery(request.getServiceName(), request.getServiceVersion(), request.getServiceGroup());

        if (serviceMetadata != null) {
            ChannelFuture future = bootstrap.connect(serviceMetadata.getIpAddr(), serviceMetadata.getPort()).sync();
            future.addListener((ChannelFutureListener) arg0 -> {
                if (future.isSuccess()) {
                    log.info("connect rpc server {} on port {} success.", serviceMetadata.getIpAddr(), serviceMetadata.getPort());
                } else {
                    log.error("connect rpc server {} on port {} failed.", serviceMetadata.getIpAddr(), serviceMetadata.getPort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(protocol);
        }
    }
}
