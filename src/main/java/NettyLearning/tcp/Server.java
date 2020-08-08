package NettyLearning.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
//    private static class ServerHandler extends SimpleChannelInboundHandler<String> {
//        private static int messageCount = 0;
//        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM-dd-hh:mm:ss");
//
//        @Override
//        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//            messageCount++;
//            System.out.println("receive from client: No." + messageCount + " " + msg);
//            Date date = new Date();
//            String formatTime = sdf.format(date);
//            ctx.writeAndFlush(Unpooled.copiedBuffer(formatTime, CharsetUtil.UTF_8));
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            cause.printStackTrace();
//        }
//    }

    private static class ServerHandlerByteBuf extends SimpleChannelInboundHandler<ByteBuf> {
        private int messageCount = 0;
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM-dd-hh:mm:ss");

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            messageCount++;
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);

            String msgString = new String(bytes, CharsetUtil.UTF_8);
            System.out.println("receive from client: No." + messageCount + " " + msgString);

            Date date = new Date();
            String formatTime = sdf.format(date);
            ctx.writeAndFlush(Unpooled.copiedBuffer(formatTime, CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }


    public void run() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServerHandlerByteBuf());
                        }
                    });
            ChannelFuture ch = serverBootstrap.bind(9968).sync();
            ch.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server().run();
    }
}
