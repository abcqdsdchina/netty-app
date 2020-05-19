package club.avence.netty.splicing.delimiter;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
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
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(NettyServer.DELIMITER, CharsetUtil.UTF_8)));
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
