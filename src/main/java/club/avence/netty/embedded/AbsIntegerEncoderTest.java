package club.avence.netty.embedded;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbsIntegerEncoderTest {

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer();
        IntStream.range(1, 6).forEach(buffer::writeInt);

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        assertTrue(channel.writeOutbound(buffer.readInt()));
        assertTrue(channel.writeOutbound(buffer.readInt()));
        assertTrue(channel.writeOutbound(buffer.readInt()));
        assertTrue(channel.writeOutbound(buffer.readInt()));
        assertTrue(channel.writeOutbound(buffer.readInt()));

        assertTrue(channel.finish());

    }

}
