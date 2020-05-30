package club.avence.netty.splicing.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    @Override
    public void channelRead(ChannelHandlerContext context, Object buffer) {
        String message = ((ByteBuf) buffer).toString(CharsetUtil.UTF_8);
        log.info("接收到消息：{}", message);
        context.write(Unpooled.copiedBuffer(NettyClient.MESSAGE, CharsetUtil.UTF_8));
        log.info("发送了第{}个响应消息：{}", COUNT.incrementAndGet(), NettyClient.MESSAGE);
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
