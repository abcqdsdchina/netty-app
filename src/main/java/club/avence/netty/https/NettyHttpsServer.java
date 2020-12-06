package club.avence.netty.https;

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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyHttpsServer {

    private static final int PORT = 6001;

    public void start() {
        try {
            SelfSignedCertificate c = new SelfSignedCertificate();
            SslContext context = SslContextBuilder.forServer(c.certificate(), c.privateKey()).build();

            final NettyHttpsServerHandler handler = new NettyHttpsServerHandler();
            ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            ServerBootstrap boostrap = new ServerBootstrap().group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            channel.pipeline().addLast(context.newHandler(channel.alloc()));
                            channel.pipeline().addLast(new HttpRequestDecoder());
                            channel.pipeline().addLast(new HttpResponseEncoder());
                            channel.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                            channel.pipeline().addLast(new HttpContentCompressor());
                            channel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture f = boostrap.bind().sync();
            f.channel().closeFuture().sync();
            group.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyHttpsServer().start();
    }

}
