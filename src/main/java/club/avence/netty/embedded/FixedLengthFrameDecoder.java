package club.avence.netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author qian3
 */
@Slf4j
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int length;

    FixedLengthFrameDecoder(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be a positive integer: " + length);
        }
        this.length = length;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        log.info("FixedLengthFrameDecoder.decode(ChannelHandlerContext context, ByteBuf in, List<Object> out)");
        int readableBytes = in.readableBytes();
        int maxLength = 10;
        if (readableBytes < maxLength) {
            while (in.readableBytes() >= length) {
                ByteBuf message = in.readBytes(length);
                out.add(message);
            }
        } else {
            in.clear();
            throw new TooLongFrameException();
        }
    }

}
