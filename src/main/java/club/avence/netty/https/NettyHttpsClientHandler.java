package club.avence.netty.https;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author qian3
 */
public class NettyHttpsClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyHttpsClientHandler.class);

    private static final String MESSAGE_TEMPLATE = "我来了！";

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        FullHttpResponse response = (FullHttpResponse) message;
        log.info("响应头信息：{}", response.headers());
        ByteBuf content = response.content();
        log.info(content.toString(CharsetUtil.UTF_8));
        content.release();
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        try {
            URI uri = new URI("/");

            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                    uri.toASCIIString(), Unpooled.wrappedBuffer(MESSAGE_TEMPLATE.getBytes(StandardCharsets.UTF_8)));

            HttpHeaders headers = request.headers();
            headers.set(HttpHeaderNames.HOST, NettyHttpsClient.HOST);
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            headers.set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            context.channel().write(request);
            context.channel().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
