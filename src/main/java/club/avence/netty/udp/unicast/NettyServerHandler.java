package club.avence.netty.udp.unicast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author qian3
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);

    private static final String MESSAGE_TEMPLATE = "Hello, {0}! Your message has been recieved.";

    @Override
    public void channelRead0(ChannelHandlerContext context, DatagramPacket packet) {
        String message = packet.content().toString(CharsetUtil.UTF_8);
        log.info("接收到消息：{}", message);
        String response = MessageFormat.format(MESSAGE_TEMPLATE, message);
        context.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
        log.info("发送了响应消息：{}", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
