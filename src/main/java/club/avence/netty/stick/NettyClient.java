package club.avence.netty.stick;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyClient {

    private String host = "localhost";
    private int port = 6001;

    public void start() throws InterruptedException {
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new NettyClientHandler());
        ChannelFuture f = boostrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }

}
