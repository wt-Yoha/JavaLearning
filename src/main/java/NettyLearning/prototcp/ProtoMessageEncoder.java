package NettyLearning.prototcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtoMessageEncoder extends MessageToByteEncoder<ProtoMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMessage protoMessage, ByteBuf byteBuf) throws Exception {
        System.out.println("ProtoMessage 编码器被调用");
        int length = protoMessage.getLength();
        byte[] content = protoMessage.getContent();
        // 分别写入数据长度和数据bytes
        byteBuf.writeInt(length);
        byteBuf.writeBytes(content);
    }
}
