package club.avence.netty.websocket;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyWebSocketServer {

    private final int PORT = 6001;

    private static final String WEB_SOCKET_PATH = "/websocket/endpoint";

    public void start() {
        try {
            SelfSignedCertificate c = new SelfSignedCertificate();
            SslContext context = SslContextBuilder.forServer(c.certificate(), c.privateKey()).build();

            final NettyWebSocketServerHandler handler = new NettyWebSocketServerHandler();
            @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            ServerBootstrap boostrap = new ServerBootstrap().group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(context.newHandler(channel.alloc()));
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            pipeline.addLast(new WebSocketServerProtocolHandler(WEB_SOCKET_PATH, null, true));
                            pipeline.addLast(new NettyWebSocketIndexHandler());
                            pipeline.addLast(handler);
                        }
                    });
            ChannelFuture f = boostrap.bind().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyWebSocketServer().start();
    }

}
