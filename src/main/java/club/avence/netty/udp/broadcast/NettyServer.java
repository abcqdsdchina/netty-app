package club.avence.netty.udp.broadcast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

/**
 * @author qian3
 */
public class NettyServer {

    private CountDownLatch latch = new CountDownLatch(1);

    @SneakyThrows(Exception.class)
    public void start() {
        final NettyServerHandler handler = new NettyServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(handler);
        ChannelFuture f = boostrap.bind(6789).sync();
        latch.await();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        new NettyServer().start();
    }

}
