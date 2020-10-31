package com.evawire.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class EvaWireFrame extends DefaultByteBufHolder {
    private final short header;

    public EvaWireFrame(short header, ByteBuf data) {
        super(data);
        this.header = header;
    }

    public EvaWireFrame(short header) {
        super(Unpooled.buffer());
        this.header = header;
    }

    public short getHeader() {
        return header;
    }

    public int getLength() {
        return this.content().readableBytes();
    }

    @Override
    public EvaWireFrame copy() {
        return (EvaWireFrame) super.copy();
    }

    @Override
    public EvaWireFrame duplicate() {
        return (EvaWireFrame) super.duplicate();
    }

    @Override
    public EvaWireFrame retainedDuplicate() {
        return (EvaWireFrame) super.retainedDuplicate();
    }

    @Override
    public EvaWireFrame replace(ByteBuf content) {
        return (EvaWireFrame) super.replace(content);
    }

    @Override
    public String toString() {
        String body = this.content().toString((CharsetUtil.UTF_8));

        for (int i = 0; i < 13; i++) {
            body = body.replace(Character.toString((char) i), "[" + i + "]");
        }

        return body;
    }

    @Override
    public EvaWireFrame retain() {
        super.retain();
        return this;
    }

    @Override
    public EvaWireFrame retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override
    public EvaWireFrame touch() {
        super.touch();
        return this;
    }

    @Override
    public EvaWireFrame touch(Object hint) {
        super.touch(hint);
        return this;
    }

    public void writeString(Object obj) {
        String string = "";

        if (obj != null) {
            string = String.valueOf(obj);
        }

        byte[] data = string.getBytes(CharsetUtil.UTF_8);
        this.writeShort(data.length);
        this.content().writeBytes(data);
    }

    public void writeShort(int val) {
        this.content().writeShort(val);
    }

    public void writeDouble(double d) {
        this.writeString(Double.toString(d));
    }

    public void writeInt(int i) {
        this.content().writeInt(i);
    }

    public void writeLong(long i) {
        this.content().writeLong(i);
    }

    public void writeBoolean(Boolean b) {
        this.content().writeByte(b ? 1 : 0);
    }

    public String readString() {
        int length = this.content().readShort();
        byte[] data = new byte[length];
        this.content().readBytes(data);

        return new String(data);
    }

    public boolean readBoolean() {
        return this.content().readByte() == 1;
    }

    public int readInt() {
        return this.content().readInt();
    }

    public short readShort() {
        return this.content().readShort();
    }

    public byte[] readBytes(int length) {
        final byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = this.content().readByte();
        }

        return bytes;
    }
}
