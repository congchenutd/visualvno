package com.fujitsu.us.oovn.core.io;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteBuffer;
import java.util.List;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.factory.BasicFactory;

public class MessageCodec extends ByteToMessageCodec<OFMessage>
{

    private BasicFactory factory = BasicFactory.getInstance();
    
    @Override
    protected void decode(ChannelHandlerContext cxt, ByteBuf in, List<Object> out) 
                                                                    throws Exception
    {
        List<OFMessage> messages = factory.parseMessages(in.nioBuffer());
        out.addAll(messages);
    }

    @Override
    protected void encode(ChannelHandlerContext cxt, OFMessage in, ByteBuf out) 
                                                                    throws Exception
    {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        in.writeTo(buffer);
        out.setBytes(0, buffer);
    }

}
