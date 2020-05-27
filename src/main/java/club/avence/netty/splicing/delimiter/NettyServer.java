package club.avence.netty.splicing.delimiter;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import lombok.Cleanup;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyServer {

    public static final String DELIMITER = "@";

    private final static int PORT = 6001;

    public void start() throws InterruptedException {
        final NettyServerHandler handler = new NettyServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap().group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(PORT))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(NettyServer.DELIMITER, CharsetUtil.UTF_8)));
                        channel.pipeline().addLast(handler);
                    }
                });
        ChannelFuture f = bootstrap.bind().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().start();
    }

}
