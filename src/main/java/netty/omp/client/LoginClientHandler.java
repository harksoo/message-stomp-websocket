package netty.omp.client;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class LoginClientHandler extends ChannelInboundHandlerAdapter {

	static final Logger logger = Logger.getLogger(LoginClientHandler.class);
	
    private final ByteBuf firstMessage;
    
    LoginClient client;
    
    // TODO 위치를 변경함.
    boolean isLogin = false;
    
    /**
     * Creates a client-side handler.
     */
    public LoginClientHandler(String msg, LoginClient client) {
    	
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

			// TODO 로긴의 처리 확인
			if(!isLogin && data.indexOf("[<Command>(LOGINUSER)][<RetVal>(Success") >= 0) {
	    		client.LoginReturn();
	    		isLogin = true;
	    	}
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