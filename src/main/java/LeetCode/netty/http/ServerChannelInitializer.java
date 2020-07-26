package LeetCode.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    static class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
            if (httpObject instanceof HttpRequest httpRequest) {
                System.out.println("pipeline hashcode = " + channelHandlerContext.pipeline().hashCode() + " httpServerHandler hash = " +
                        this.hashCode());
                System.out.println("msg type = " + httpObject.getClass());
                System.out.println("client address = " + channelHandlerContext.channel().remoteAddress());
                // 获取uri解析指定资源
                URI uri = new URI(httpRequest.uri());
                if ("/favicon.ico".equals(uri.getPath())) {
                    System.out.println("请求了favicon.ico，不做响应");
                    return;
                }
                ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
                // 构造一个http响应
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
                channelHandlerContext.writeAndFlush(response);
            }
        }
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 增加一个http解析器
        pipeline.addLast("MyHttpServerDecoder", new HttpServerCodec());
        // 增加一个处理响应的Handler
        pipeline.addLast("MyHttpServerHandler", new ServerHandler());
    }
}
