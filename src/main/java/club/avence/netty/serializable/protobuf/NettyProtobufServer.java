package club.avence.netty.serializable.protobuf;

import club.avence.netty.ClosableNioEventLoopGroup;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.Cleanup;

import java.net.InetSocketAddress;

/**
 * ./protoc --proto_path=D:\projects\JavaDemo\src\main\java\club\avence\netty\serializable\protobuf --java_out=D:\projects\JavaDemo\src\main\java Employee.proto
 */
public class NettyProtobufServer {

    private final int port = 6001;

    public void start() throws InterruptedException {
        final NettyProtobufServerHandler handler = new NettyProtobufServerHandler();
        @Cleanup ClosableNioEventLoopGroup group = new ClosableNioEventLoopGroup();
        ServerBootstrap boostrap = new ServerBootstrap().group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        channel.pipeline().addLast(new ProtobufDecoder(EmployeeProto.Employee.getDefaultInstance()));
                        channel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        channel.pipeline().addLast(new ProtobufEncoder());
                        channel.pipeline().addLast(handler);
                    }
                });
        ChannelFuture f = boostrap.bind().sync();
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyProtobufServer().start();
    }

}
