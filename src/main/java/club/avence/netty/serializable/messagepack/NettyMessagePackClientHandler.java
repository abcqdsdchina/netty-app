package club.avence.netty.serializable.messagepack;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyMessagePackClientHandler extends SimpleChannelInboundHandler<Employee> {

    private static final Logger log = LoggerFactory.getLogger(NettyMessagePackClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext context, Employee message) {
        log.info("接收到响应数据：{}", message);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        Employee employee = new Employee();
        employee.setCode("1128068").setName("zhouyichen");
        context.writeAndFlush(employee);
        log.info("发送了请求数据：{}", employee);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
