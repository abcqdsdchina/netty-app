package club.avence.netty.serializable.messagepack;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.net.InetSocketAddress;

public class NettyMessagePackServer {

    private static final int PORT = 6001;

    public void start() {
        try {
            final NettyMessagePackServerHandler handler = new NettyMessagePackServerHandler();
            ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            ServerBootstrap boostrap = new ServerBootstrap().group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
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
            group.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyMessagePackServer().start();
    }

}
