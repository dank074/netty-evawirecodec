package com.evawire.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class EvaWireDecoder extends ByteToMessageDecoder {
    private final int maxPayloadLength;
    private final boolean closeOnProtocolViolation;

    public EvaWireDecoder(int maxPayloadLength, boolean closeOnProtocolViolation) {
        this.maxPayloadLength = maxPayloadLength;
        this.closeOnProtocolViolation = closeOnProtocolViolation;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        int length = in.readInt();
        if(length > maxPayloadLength) {
            protocolViolation(ctx, in, new CorruptedFrameException("Message payload exceeded maximum payload allowed"));
            return;
        }

        if(in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        short header = in.readShort();
        out.add(new EvaWireFrame(header, in.readBytes(length - 2)));
    }

    private void protocolViolation(ChannelHandlerContext ctx, ByteBuf in, CorruptedFrameException ex) {
        int readableBytes = in.readableBytes();
        if (readableBytes > 0) {
            // Fix for memory leak, caused by ByteToMessageDecoder#channelRead:
            // buffer 'cumulation' is released ONLY when no more readable bytes available.
            in.skipBytes(readableBytes);
        }
        if (ctx.channel().isActive() && closeOnProtocolViolation) {
            ctx.close();
        }
        throw ex;
    }
}
