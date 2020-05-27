package club.avence.netty.splicing.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qian3
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final int MESSAGE_COUNT = 100;

    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf message) {
        log.info("接收到响应数据：{}", message.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "Qian(" + i + ")";
            context.writeAndFlush(Unpooled.copiedBuffer(message + NettyServer.DELIMITER, CharsetUtil.UTF_8));
            log.info("发送了数据：{}", message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
