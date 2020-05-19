package club.avence.netty.https;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyHttpsClient {

    public static final String HOST = "localhost";
    public static final int PORT = 6001;

    @SneakyThrows
    public void start() {
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        Bootstrap boostrap = new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(HOST, PORT))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    @SneakyThrows
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(SslContextBuilder.forClient()
                                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                                .newHandler(channel.alloc()));
                        channel.pipeline().addLast(new HttpClientCodec());
                        channel.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                        channel.pipeline().addLast(new HttpContentDecompressor());
                        channel.pipeline().addLast(new NettyHttpsClientHandler());
                    }
                });
        ChannelFuture f = boostrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        new NettyHttpsClient().start();
    }

}
