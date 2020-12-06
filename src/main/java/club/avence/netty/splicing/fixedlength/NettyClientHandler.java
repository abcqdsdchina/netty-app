package club.avence.netty.splicing.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf message) {
        log.info("接收到响应数据：{}", message.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        for (int i = 0; i < 100; i++) {
            context.writeAndFlush(Unpooled.copiedBuffer(NettyClient.MESSAGE, CharsetUtil.UTF_8));
            log.info("发送了第{}个数据：{}", i, NettyClient.MESSAGE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
