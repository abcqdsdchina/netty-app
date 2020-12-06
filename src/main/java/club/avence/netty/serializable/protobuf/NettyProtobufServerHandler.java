package club.avence.netty.serializable.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NettyProtobufServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NettyProtobufServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) {
        EmployeeProto.Employee employee = (EmployeeProto.Employee) message;
        log.info("接收到请求数据{}", employee);
        EmployeeProto.Employee.Builder builder = EmployeeProto.Employee.newBuilder();
        builder.setCode(employee.getCode()).setName(employee.getName()).setIdCard("370281198902280558");
        context.writeAndFlush(builder.build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable t) {
        log.error(t.getMessage(), t);
        context.close();
    }

}
