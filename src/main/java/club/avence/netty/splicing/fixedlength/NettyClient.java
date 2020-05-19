package club.avence.netty.splicing.fixedlength;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyClient {

    public static final String MESSAGE = "QIAN";

    private String host = "localhost";
    private int port = 6001;

    public void start() throws InterruptedException {
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new FixedLengthFrameDecoder(NettyClient.MESSAGE.length()));
                        channel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        ChannelFuture f = boostrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }

}
