package com.fujitsu.us.oovn.core.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server
{
    private final int port = 6634;
    private static Server _instance;
    
    public static Server getInstance()
    {
        if(_instance == null)
            _instance = new Server();
        return _instance;
    }

    public void run() throws Exception
    {
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("framer",
                                         new DelimiterBasedFrameDecoder(8192,
                                                                        Delimiters.lineDelimiter()));
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast("handler", new SwitchChannelHandler(Server.getInstance()));
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.TCP_NODELAY,  true)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } 
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup  .shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        getInstance().run();
    }
}


//class EchoServerHandler extends ChannelHandlerAdapter
//{
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg)
//    {
//        System.out.println((String) msg);
//        ctx.write(msg);
//        ctx.flush();
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
//
//class TimeServerHandler extends ChannelHandlerAdapter
//{
//
//    @Override
//    public void channelActive(final ChannelHandlerContext ctx)
//    {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        Date date = new Date();
//        byte[] bytes = dateFormat.format(date).getBytes();
//        final ByteBuf buffer = ctx.alloc().buffer(bytes.length);
//        buffer.writeBytes(bytes);
//        final ChannelFuture f = ctx.writeAndFlush(buffer);
//        
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) {
//                assert f == future;
//                ctx.close();
//            }
//        });
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
//}
