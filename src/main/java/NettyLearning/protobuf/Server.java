package NettyLearning.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {
    public static class ServerHandler extends SimpleChannelInboundHandler<ProjectProto.Student> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ProjectProto.Student student) throws Exception {
            System.out.println("Copy client msg.");
            System.out.println(student);
//            channelHandlerContext.writeAndFlush(student);
            channelHandlerContext.writeAndFlush("Hello Client");
        }
    }
    public void run(){
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // add an protoStudent class decoder
                            pipeline.addLast(new ProtobufDecoder(ProjectProto.Student.getDefaultInstance()));
//                            pipeline.addLast(new ProtobufEncoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture sync = serverBootstrap.bind(9968).sync();
            System.out.println("Server start...");
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
