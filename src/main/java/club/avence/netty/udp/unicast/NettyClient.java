package club.avence.netty.udp.unicast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.Cleanup;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyClient {

    private static final String HOST = "localhost";
    private static final int PORT = 6001;

    public void start() {
        try {
            @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            Bootstrap boostrap = new Bootstrap().group(group)
                    .channel(NioDatagramChannel.class)
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    .handler(new NettyClientHandler());
            Channel channel = boostrap.bind(0).sync().channel();
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("Qian", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", 6789)));
            ChannelFuture future = channel.closeFuture();
            future.await(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyClient().start();
    }

}
