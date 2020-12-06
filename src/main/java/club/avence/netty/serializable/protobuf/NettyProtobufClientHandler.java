package club.avence.netty.serializable.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyProtobufClientHandler extends SimpleChannelInboundHandler<EmployeeProto.Employee> {

    private static final Logger log = LoggerFactory.getLogger(NettyProtobufClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext context, EmployeeProto.Employee message) {
        log.info("接收到响应数据：{}", message);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        EmployeeProto.Employee.Builder builder = EmployeeProto.Employee.newBuilder();
        builder.setCode("1128068").setName("zhouyichen");
        context.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
