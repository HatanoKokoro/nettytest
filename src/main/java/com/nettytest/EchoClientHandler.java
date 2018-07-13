package com.nettytest;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter{

    private static final String ECHO = "test hello world $_";
    private static int counter;

    public EchoClientHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("this is" +  ++counter + "msg:"+msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("clientchannelReadComplete");
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 50; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO.getBytes()));
        }
        System.out.println("clientchannelActive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("clientexceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
}
