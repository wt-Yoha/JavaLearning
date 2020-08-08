package NettyLearning.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 演示tcp的粘包和拆包
 */
public class Client {
    private static class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        private int messageCount = 0;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            int lengthOfMessage = 10;
            for (int i = 0; i < lengthOfMessage; i++) {
                String msg = "Hello server " + i + " ";
                ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);
            String msgString = new String(bytes, CharsetUtil.UTF_8);
            messageCount++;
            System.out.println("receive from server No." + messageCount + " " + msgString);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
    public void run() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture ch = bootstrap.connect("localhost", 9968).sync();
            ch.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
    public static void main(String[] args) {
        new Client().run();
    }
}
