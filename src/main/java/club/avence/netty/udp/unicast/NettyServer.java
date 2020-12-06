package club.avence.netty.udp.unicast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Cleanup;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author qian3
 */
public class NettyServer {

    private static final int PORT = 6001;

    private final CountDownLatch latch = new CountDownLatch(1);

    public void start() {
        try {
            final NettyServerHandler handler = new NettyServerHandler();
            @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            Bootstrap boostrap = new Bootstrap().group(group)
                    .channel(NioDatagramChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .handler(handler);
            ChannelFuture f = boostrap.bind(6789).sync();
            latch.await();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }

}
