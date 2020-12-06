package club.avence.netty.udp.broadcast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.concurrent.CountDownLatch;

/**
 * @author qian3
 */
public class NettyServer {

    private final CountDownLatch latch = new CountDownLatch(1);

    public void start() {
        try {
            final NettyServerHandler handler = new NettyServerHandler();
            ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            Bootstrap boostrap = new Bootstrap().group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
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
