package club.avence.netty.http;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyHttpClient {

    public static final String HOST = "localhost";
    public static final int PORT = 6001;

    public void start() throws InterruptedException {
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new HttpClientCodec());
                        channel.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                        channel.pipeline().addLast(new HttpContentDecompressor());
                        channel.pipeline().addLast(new NettyHttpClientHandler());
                    }
                });
        ChannelFuture f = boostrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyHttpClient().start();
    }

}
