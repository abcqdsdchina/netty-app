package club.avence.netty.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final String MESSAGE_TEMPLATE = "你好！消息已经收到.";

    private ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext context, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame f = (TextWebSocketFrame) frame;
            log.info("接收到了客户端的消息：{}", f.text());
            context.channel().write(new TextWebSocketFrame(MESSAGE_TEMPLATE));
        } else {
            throw new UnsupportedOperationException("不支持的帧类型.");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object event) {
        log.info("接收到了用户事件：{}", event.getClass());
        if (event instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("握手成功.ChannelGroupSize：{}", group.size());
            group.writeAndFlush(new TextWebSocketFrame("有新的连接加入."));
            group.add(context.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
