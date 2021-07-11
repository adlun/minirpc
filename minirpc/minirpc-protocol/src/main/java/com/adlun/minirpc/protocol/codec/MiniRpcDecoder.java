package com.adlun.minirpc.protocol.codec;

import com.adlun.minirpc.core.common.MiniRpcRequest;
import com.adlun.minirpc.core.common.MiniRpcResponse;
import com.adlun.minirpc.protocol.protocol.MiniRpcProtocol;
import com.adlun.minirpc.protocol.protocol.MsgHeader;
import com.adlun.minirpc.protocol.protocol.MsgType;
import com.adlun.minirpc.protocol.protocol.ProtocolConstants;
import com.adlun.minirpc.protocol.serialization.IRpcSerialization;
import com.adlun.minirpc.protocol.serialization.impl.HessianSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MiniRpcDecoder extends ByteToMessageDecoder {

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
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LENGTH) {
            return;
        }
        in.markReaderIndex();

        short magic = in.readShort();
        if (Short.compare(magic, ProtocolConstants.MAGIC) != 0) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        byte version = in.readByte();
        byte serializationAlgorithm = in.readByte();
        byte msgType = in.readByte();
        byte msgStatus = in.readByte();
        long msgId = in.readLong();
        int msgLength = in.readInt();
        if (in.readableBytes() < msgLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[msgLength];
        in.readBytes(data);

        MsgType msgTypeEnum = MsgType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerializationAlgorithm(serializationAlgorithm);
        header.setStatus(msgStatus);
        header.setMsgId(msgId);
        header.setMsgType(msgType);
        header.setMsgLength(msgLength);

        IRpcSerialization rpcSerialization = new HessianSerialization();
        switch (msgTypeEnum) {
            case REQUEST:
                MiniRpcRequest request = rpcSerialization.deserialize(data, MiniRpcRequest.class);
                if (request != null) {
                    MiniRpcProtocol<MiniRpcRequest> protocol = new MiniRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                MiniRpcResponse response = rpcSerialization.deserialize(data, MiniRpcResponse.class);
                if (response != null) {
                    MiniRpcProtocol<MiniRpcResponse> protocol = new MiniRpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                // TODO
                break;
        }

    }
}
