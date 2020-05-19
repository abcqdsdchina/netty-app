package club.avence.netty.stick;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyServer {

    private final int port = 6001;

    public void start() throws InterruptedException {
        final NettyServerHandler handler = new NettyServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        ServerBootstrap boostrap = new ServerBootstrap().group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(handler);
                    }
                });
        ChannelFuture f = boostrap.bind().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().start();
    }

}
