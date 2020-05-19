package club.avence.netty.serializable.messagepack;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.Cleanup;

import java.net.InetSocketAddress;

public class NettyMessagePackClient {

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
                        channel.pipeline().addLast(new LengthFieldPrepender(2));
                        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        channel.pipeline().addLast(new MessagePackDecoder());
                        channel.pipeline().addLast(new MessagePackEncoder());
                        channel.pipeline().addLast(new NettyMessagePackClientHandler());
                    }
                });
        ChannelFuture f = boostrap.connect().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyMessagePackClient().start();
    }

}
