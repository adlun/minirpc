package com.adlun.minirpc.protocol.codec;

import com.adlun.minirpc.protocol.protocol.MiniRpcProtocol;
import com.adlun.minirpc.protocol.protocol.MsgHeader;
import com.adlun.minirpc.protocol.serialization.IRpcSerialization;
import com.adlun.minirpc.protocol.serialization.impl.HessianSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MiniRpcEncoder extends MessageToByteEncoder<MiniRpcProtocol<Object>> {


    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
    +---------------------------------------------------------------+
    | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
    +---------------------------------------------------------------+
    |                   数据内容 （长度不定）                          |
    +---------------------------------------------------------------+
    */
    @Override
    protected void encode(ChannelHandlerContext ctx, MiniRpcProtocol<Object> msg, ByteBuf out) throws Exception {
        MsgHeader header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getSerializationAlgorithm());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getMsgId());
        IRpcSerialization rpcSerialization = new HessianSerialization();
        byte[] data = rpcSerialization.serialize(msg.getBody());
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
