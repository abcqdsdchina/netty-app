package club.avence.netty.udp.broadcast;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @author qian3
 */
public class NettyClient {

    public void start() {
        try {
            ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
            Bootstrap boostrap = new Bootstrap().group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new NettyClientHandler());
            Channel channel = boostrap.bind(0).sync().channel();
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("Qian", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255", 6789)));
            ChannelFuture future = channel.closeFuture();
            future.await(3000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new NettyClient().start();
    }

}
