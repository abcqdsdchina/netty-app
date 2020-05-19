package club.avence.netty.serializable.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Cleanup;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

public class MessagePackEncoder extends MessageToByteEncoder<Employee> {

    @Override
    protected void encode(ChannelHandlerContext context, Employee employee, ByteBuf out) throws Exception {
        @Cleanup MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        packer.packString(employee.getCode());
        packer.packString(employee.getName());
        packer.packString(employee.getIdCard());
        out.writeBytes(packer.toByteArray());
    }

}
