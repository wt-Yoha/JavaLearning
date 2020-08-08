package NettyLearning.prototcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ProtoMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("ProtoMessage 解码器被调用");
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        ProtoMessage protoMessage = new ProtoMessage();
        protoMessage.setLength(length);
        protoMessage.setContent(bytes);

        list.add(protoMessage);
    }
}
