package NettyLearning.protobuf3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 使用LengthFiledPrepender 解决使用protpbuf时的粘包问题
 */
public class Server {
    public static class ServerHandler extends SimpleChannelInboundHandler<ProtoClass.Message> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProtoClass.Message message) throws Exception {
            switch (message.getDataType()) {
                case STUDENT:
                    ProtoClass.Student student = message.getStudent();
                    System.out.println(channelHandlerContext.channel().remoteAddress() + ": " + student);
                    channelHandlerContext.writeAndFlush("Copy message student");
                    break;
                case TEACHER:
                    ProtoClass.Teacher teacher = message.getTeacher();
                    System.out.println(channelHandlerContext.channel().remoteAddress() + ": " + teacher);
                    channelHandlerContext.writeAndFlush("Copy message teacher");
                    break;
                default:
                    System.out.println("Unrecognized data type!");
                    channelHandlerContext.writeAndFlush("Unrecognized data type!");
            }
            System.out.println("server send the message.");
        }
    }

    public void run() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap().group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            pipeline.addLast("ProtoDecoder",new ProtobufDecoder(ProtoClass.Message.getDefaultInstance()));
                            pipeline.addLast("frameEncoder",new LengthFieldPrepender(2));
                            pipeline.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(9968).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new Server().run();
    }
}
