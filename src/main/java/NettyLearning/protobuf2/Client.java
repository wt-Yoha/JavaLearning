package NettyLearning.protobuf2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class Client {
    private static class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ProtoClass.Message message;
            int flag = new Random().nextInt(100);
            if (flag % 2 == 0) {
                message = ProtoClass.Message.newBuilder()
                        .setDataType(ProtoClass.Message.DataType.STUDENT)
                        .setStudent(ProtoClass.Student.newBuilder()
                                .setId(2345)
                                .setName("Lucy")
                                .setGrade(86.8).build())
                        .build();
            } else {
                message = ProtoClass.Message.newBuilder()
                        .setDataType(ProtoClass.Message.DataType.TEACHER)
                        .setTeacher(ProtoClass.Teacher.newBuilder()
                                .setName("Bob")
                                .setDept("Computer Science")
                                .setClass_("Class 9").build())
                        .build();
            }
            ctx.writeAndFlush(message);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof String) {
                System.out.println((String) msg);
            } else {
                System.out.println("receive obj not a string");
            }
        }
    }
    public void run() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 9968).sync();
            channelFuture.channel().closeFuture().sync();
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
