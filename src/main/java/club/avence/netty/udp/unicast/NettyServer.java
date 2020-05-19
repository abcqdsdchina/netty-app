package club.avence.netty.udp.unicast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author qian3
 */
public class NettyServer {

    private static final int PORT = 6001;

    private CountDownLatch latch = new CountDownLatch(1);

    @SneakyThrows(Exception.class)
    public void start() {
        final NettyServerHandler handler = new NettyServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioDatagramChannel.class)
                .localAddress(new InetSocketAddress(PORT))
                .handler(handler);
        ChannelFuture f = boostrap.bind(6789).sync();
        latch.await();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }

}
