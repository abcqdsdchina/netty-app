package club.avence.netty.classical;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);

    private static final String HOST = "localhost";
    private static final int PORT = 6001;

    public void start() throws InterruptedException {
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .handler(new NettyClientHandler());
        ChannelFuture f = bootstrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }

}
