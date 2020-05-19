package club.avence.netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qian3
 */
@Slf4j
public class FixedLengthFrameDecoderTest {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer();
        Stream.of('a', 'b', 'c', 'd', 'e', 'f').forEach(buffer::writeByte);

        ByteBuf mirror = buffer.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(mirror.readBytes(1)));
        assertFalse(channel.writeInbound(mirror.readBytes(1)));
        assertTrue(channel.writeInbound(mirror.readBytes(1)));

        assertTrue(channel.writeInbound(mirror.readBytes(3)));
        assertTrue(channel.finish());

        ByteBuf bytes = channel.readInbound();
        assertEquals(3, bytes.readableBytes());
    }

}
