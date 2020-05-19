package club.avence.netty.https;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qian3
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyHttpsServerHandler extends ChannelInboundHandlerAdapter {

    private static final String MESSAGE_TEMPLATE = "你好！消息已经收到.";

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        @Cleanup("release") FullHttpRequest request = (FullHttpRequest) message;
        log.info("path = {}", request.uri());
        log.info("method = {}", request.method());
        log.info("content = {}", request.content().toString(CharsetUtil.UTF_8));

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(MESSAGE_TEMPLATE, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        context.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
