package club.avence.netty;

/**
 * @author qian3
 */
public class ClosableNioEventLoopGroup extends io.netty.channel.nio.NioEventLoopGroup {

    public void close() throws InterruptedException {
        this.shutdownGracefully().sync();
    }
}
