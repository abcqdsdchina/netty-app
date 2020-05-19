package club.avence.netty.http;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

public class NettyHttpServer {

    private final int port = 6001;

    @SneakyThrows
    public void start() {
        final NettyHttpServerHandler handler = new NettyHttpServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        ServerBootstrap boostrap = new ServerBootstrap().group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new HttpRequestDecoder());
                        channel.pipeline().addLast(new HttpResponseEncoder());
                        channel.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                        channel.pipeline().addLast(new HttpContentCompressor());
                        channel.pipeline().addLast(handler);
                    }
                });
        ChannelFuture f = boostrap.bind().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyHttpServer().start();
    }

}
