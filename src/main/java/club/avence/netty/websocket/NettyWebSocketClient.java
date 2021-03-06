package club.avence.netty.websocket;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author qiancheng
 */
public class NettyWebSocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 6001;

    private static final String WEB_SOCKET_PATH = "ws://" + HOST + ":" + 6001 + "/websocket/endpoint";

    public void start() {
        try {
            ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            Bootstrap boostrap = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            try {
                                channel.pipeline().addLast(SslContextBuilder.forClient()
                                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build().newHandler(channel.alloc()));
                                channel.pipeline().addLast(new HttpClientCodec());
                                channel.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));
                                channel.pipeline().addLast(WebSocketClientCompressionHandler.INSTANCE);
                                channel.pipeline().addLast(new WebSocketClientProtocolHandler(
                                        WebSocketClientHandshakerFactory.newHandshaker(new URI(WEB_SOCKET_PATH), WebSocketVersion.V13,
                                                null, true, new DefaultHttpHeaders()), true
                                ));
                                channel.pipeline().addLast(new NettyWebSocketClientHandler());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            ChannelFuture f = boostrap.connect().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyWebSocketClient().start();
    }

}
