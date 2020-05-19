package club.avence.netty.serializable.messagepack;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;

public class NettyMessagePackServer {

    private final int port = 6001;

    @SneakyThrows
    public void start() {
        final NettyMessagePackServerHandler handler = new NettyMessagePackServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        ServerBootstrap boostrap = new ServerBootstrap().group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new LengthFieldPrepender(2));
                        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        channel.pipeline().addLast(new MessagePackDecoder());
                        channel.pipeline().addLast(new MessagePackEncoder());
                        channel.pipeline().addLast(handler);
                    }
                });
        ChannelFuture f = boostrap.bind().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyMessagePackServer().start();
    }

}
