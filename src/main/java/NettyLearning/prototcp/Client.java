package NettyLearning.prototcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import java.util.Random;

/**
 * 演示tcp的粘包和拆包
 */
public class Client {
    private static class ClientHandler extends SimpleChannelInboundHandler<ProtoMessage> {
        private int messageCount = 0;
        private final String article = "bind 函数的各层分工很明显，主要就是inet_bind函数了，在注释里说的很明确了，bind 是绑定本地地址，它不负责对端地址，一般用于服务器端，客户端是系统指定的。";

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String[] strings = article.split("，");
            String msg;
            byte[] bytes;
            int count = new Random().nextInt(10), idx;
            ProtoMessage protoMessage;

            for (int i = 0; i < count; i++) {
                idx = new Random().nextInt(strings.length);
                msg = strings[idx];
                bytes = msg.getBytes(CharsetUtil.UTF_8);

                protoMessage = new ProtoMessage();
                protoMessage.setLength(bytes.length);
                protoMessage.setContent(bytes);

                ctx.writeAndFlush(protoMessage);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ProtoMessage msg) throws Exception {
            messageCount++;
            System.out.println("收到服务器消息: No." + messageCount + " " + new String(msg.getContent(), CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }

    private static class ClientHandlerString extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.println("收到服务器String类型消息: " + s);
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

                            pipeline.addLast(new ProtoMessageDecoder());
                            pipeline.addLast(new ProtoMessageEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ClientHandlerString());
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
