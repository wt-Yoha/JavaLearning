package NettyLearning.prototcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.text.SimpleDateFormat;

public class Server {

    private static class ServerHandlerByteBuf extends SimpleChannelInboundHandler<ProtoMessage> {
        private int messageCount = 0;
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM-dd-hh:mm:ss");

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ProtoMessage msg) throws Exception {
            messageCount++;
            System.out.println("收到客户端消息: No." + messageCount + " " + new String(msg.getContent(), CharsetUtil.UTF_8));

//            ctx.writeAndFlush(msg);
            ctx.writeAndFlush("服务器收到消息_" + messageCount);
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

                            pipeline.addLast(new ProtoMessageDecoder());
                            pipeline.addLast(new ProtoMessageEncoder());
                            pipeline.addLast(new StringEncoder());
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
