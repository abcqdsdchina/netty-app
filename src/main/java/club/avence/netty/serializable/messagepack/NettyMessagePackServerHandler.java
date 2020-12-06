package club.avence.netty.serializable.messagepack;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NettyMessagePackServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyMessagePackServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        Employee employee = (Employee) message;
        log.info("接收到请求数据{}", employee);
        employee.setIdCard("370281198902280558");
        context.writeAndFlush(employee);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
