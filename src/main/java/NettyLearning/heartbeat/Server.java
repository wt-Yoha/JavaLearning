package NettyLearning.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * netty 心跳机制
 */
public class Server {
    static class MyServerHandler extends ChannelInboundHandlerAdapter {
        /**
         * 用户事件触发器
         * @param ctx
         * @param evt
         * @throws Exception
         */
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                String msg = null;
                switch (event.state()) {
                    case READER_IDLE:
                        msg = "读超时";
                        break;
                    case WRITER_IDLE:
                        msg = "写超时";
                        break;
                    case ALL_IDLE:
                        msg = "读写超时";
                }
                System.out.println(ctx.channel().remoteAddress() + " " + msg);
                // 超时则关闭通道，节省服务器资源
                ctx.channel().close();
//                ctx.close();
            }
        }
    }

    void run() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler()) // 为bossGroup添加Handler，记录日志
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /* netty 提供的空闲状态处理器
                    说明 1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                        2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
                        3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                        4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
                        5. 文档说明
                        6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个 handler 去处理 通过调用(触发)下一个 handler 的
                           userEventTiggered , 在该方法中去处理 IdleStateEvent(读空闲，写空闲，读写空闲)
                    * */
                    pipeline.addLast(new IdleStateHandler(13, 5, 2, TimeUnit.SECONDS));
                    pipeline.addLast(new MyServerHandler());
                }
            });
            ChannelFuture sync = serverBootstrap.bind(9968).sync();
            System.out.println("服务器启动");
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
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
