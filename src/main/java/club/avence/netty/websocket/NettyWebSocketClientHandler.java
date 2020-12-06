package club.avence.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author c_qiancheng
 */
public class NettyWebSocketClientHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(NettyWebSocketClientHandler.class);

    private static final String MESSAGE_TEMPLATE = "我来了！";

    @Override
    public void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame f = (TextWebSocketFrame) frame;
            log.info("接收到了消息：{}", f.text());
        } else {
            throw new UnsupportedOperationException("不支持的消息类型.");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        log.info("Channel is active.");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        log.info("接收到了用户事件：{}", event.getClass());
        if (event == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            log.info("发送消息：{}", MESSAGE_TEMPLATE);
            TextWebSocketFrame frame = new TextWebSocketFrame(MESSAGE_TEMPLATE);
            context.writeAndFlush(frame);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
