package netty.omp.client;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class RouteClientHandler extends ChannelInboundHandlerAdapter {
	
	static final Logger logger = Logger.getLogger(RouteClientHandler.class);

    private final ByteBuf firstMessage;    
    private RouteClient client;

    public RouteClientHandler(String msg, RouteClient client) {
    	
    	this.client = client;
        firstMessage = Unpooled.buffer(msg.getBytes().length);
        firstMessage.writeBytes(msg.getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    	logger.debug("channelActive");
    	//TODO 초기에 로그인 처리를 해야함.
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

		try {
			ByteBuf buf = (ByteBuf) msg;
			String data = buf.toString(Charset.forName("UTF-8"));
			logger.debug("channelRead() : \r\n" + data);
			
			client.getQueue().add(data);

//			// TODO 로긴의 처리 확인
//			if (data.indexOf("[<Command>(LOGINUSER)][<RetVal>(Success") >= 0) {
//
//			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    	logger.debug("channelReadComplete");
    	// message가 끊겨서 옮으로 합시는 작업을 처리한다.
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}