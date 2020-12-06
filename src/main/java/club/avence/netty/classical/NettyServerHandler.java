package club.avence.netty.classical;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author qian3
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);

    private static final String MESSAGE_TEMPLATE = "Hello, {0}! Your message has been received.";

    @Override
    public void channelRead(ChannelHandlerContext context, Object buffer) {
        String message = ((ByteBuf) buffer).toString(CharsetUtil.UTF_8);
        log.info("接收到消息：{}", message);
        String response = MessageFormat.format(MESSAGE_TEMPLATE, message);
        context.write(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
        log.info("发送了响应消息：{}", response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.writeAndFlush(Unpooled.EMPTY_BUFFER)
               .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
