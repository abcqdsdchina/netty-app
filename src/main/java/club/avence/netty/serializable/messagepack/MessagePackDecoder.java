package club.avence.netty.serializable.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.SneakyThrows;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author c_qiancheng
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(MessagePackDecoder.class);

    @Override
    @SneakyThrows
    protected void decode(ChannelHandlerContext context, ByteBuf message, List<Object> out) {
        int length = message.readableBytes();
        byte[] bytes = new byte[length];
        message.getBytes(message.readerIndex(), bytes, 0, length);

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes);
        Employee employee = new Employee();
        employee.setCode(unpacker.unpackString()).setName(unpacker.unpackString()).setIdCard(unpacker.unpackString());
        out.add(employee);
    }

}
