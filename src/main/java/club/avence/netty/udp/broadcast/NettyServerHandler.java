package club.avence.netty.udp.broadcast;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * @author qian3
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String MESSAGE_TEMPALTE = "Hello, {0}! Your message has been recieved.";

    @Override
    public void channelRead0(ChannelHandlerContext context, DatagramPacket packet) {
        String message = packet.content().toString(CharsetUtil.UTF_8);
        log.info("接收到消息：{}", message);
        String response = MessageFormat.format(MESSAGE_TEMPALTE, message);
        context.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8), packet.sender()));
        log.info("发送了响应消息：{}", response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
