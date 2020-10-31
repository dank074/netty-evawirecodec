package com.evawire.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class EvaWireEncoder extends MessageToMessageEncoder<EvaWireFrame> implements IEvaWireEncoder {
    /**
     * 6 additional bytes needed
     * (4 for payload length, 2 for packet header)
     */
    private static final int additionalLength = 6;

    protected void encode(ChannelHandlerContext ctx, EvaWireFrame msg, List<Object> out) throws Exception {
        int size = msg.getLength() + additionalLength;
        ByteBuf buf = null;
        try {
            buf = ctx.alloc().buffer(size);
            buf.writeInt(msg.getLength());
            buf.writeShort(msg.getHeader());
            buf.writeBytes(msg.content());
            out.add(buf);
        } finally {
            if(buf != null) buf.release();
        }
    }
}
