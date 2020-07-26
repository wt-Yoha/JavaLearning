package NettyLearning.DemoChatRoom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    static class ServerChannelHandler extends SimpleChannelInboundHandler<String>{
        private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            // 广播上线消息
            channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天" + sdf.format(new Date()) + "\n");
            // 将本channel加入channelGroup
            channelGroup.add(channel);
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            Channel channel = ctx.channel();
            channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
            System.out.println("当前在线人数："+channelGroup.size());
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().remoteAddress()+" 上线了");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + " 离线了\n");
            System.out.println("[客户端]" + ctx.channel().remoteAddress() + " 离线了\n");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            Channel channel = channelHandlerContext.channel();
            for (Channel ch: channelGroup) {
                if (ch != channel) {
                    ch.writeAndFlush("[客户端]" + channel.remoteAddress() + " 发送了消息：" + s + "\n");
                } else {
                    ch.writeAndFlush("[自己]" + channel.remoteAddress() + " 发送了消息：" + s + "\n");
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("StringDecoder", new StringDecoder());
                            pipeline.addLast("StringEncoder", new StringEncoder());
                            pipeline.addLast(new ServerChannelHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(9968).sync();
            System.out.println("服务器启动。。。");
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Server().run();
    }
}
