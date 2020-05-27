package club.avence.netty.stick;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qian3
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final String MESSAGE_TEMPLATE = "Hello, {0}! Your message has been recieved.";

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext context, Object buffer) {
        String message = ((ByteBuf) buffer).toString(CharsetUtil.UTF_8);
        log.info("接收到消息：{}", message);
        log.info("接收到的数量：{}", COUNT.incrementAndGet());
        String response = MessageFormat.format(MESSAGE_TEMPLATE, message);
        context.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
        log.info("发送了响应消息：{}", response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
