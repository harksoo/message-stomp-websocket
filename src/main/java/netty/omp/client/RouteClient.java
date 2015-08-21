package netty.omp.client;

import javax.net.ssl.SSLException;

import org.apache.log4j.Logger;

import com.telcoware.omp.OccmRecvQueue;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class RouteClient implements Runnable {

	static final Logger logger = Logger.getLogger(RouteClient.class);
	
	boolean SSL = System.getProperty("ssl") != null;
	String HOST = System.getProperty("host", "10.10.3.152");
	int PORT = Integer.parseInt(System.getProperty("port", "19595"));
	// int SIZE = Integer.parseInt(System.getProperty("size", "256"));
	
	String initMsg = "[<MSGSIZE>( 120)][<Command>(LOGINROUTE)][<Userid>(_mmcx_)][<Passwd>(_mmcx_)][<Ip>(10.10.105.127)][<Hostname>(harksoo-pc)][<Mac>(unknown)]";
	
	//String initMsg = "[<MSGSIZE>( 143)][<Command>(LOGINROUTE)][<Userid>(superusr)][<Passwd>(1234)][<Ip>(10.10.105.127)][<Hostname>(harksoo-pc)][<Mac>(00:30:67:DD:D5:E1)][<PID>(8052)]";
	
	RouteClient client;
	
	OccmRecvQueue queue;
	
	public RouteClient(OccmRecvQueue queue){
		client = this;
		this.queue = queue;
	}
	
	public OccmRecvQueue getQueue() {
		return queue;
	}

	public void setQueue(OccmRecvQueue queue) {
		this.queue = queue;
	}

	
	ChannelFuture f = null;

	public void run() {

		// Configure SSL.git
		final SslContext sslCtx;

		SslContext sslCtxTemp = null;
		if (SSL) {
			try {
				sslCtxTemp = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
						.build();
			} catch (SSLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			sslCtxTemp = null;
		}

		sslCtx = sslCtxTemp;

		// Configure the clientLogin.
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							if (sslCtx != null) {
								p.addLast(sslCtx.newHandler(ch.alloc(), HOST, PORT));
							}
							// p.addLast(new LoggingHandler(LogLevel.INFO));
							// p.addLast(new LoginClientHandler());
							p.addLast(new OccmDecoder(), new RouteClientHandler(initMsg, client));
						}
					});

			// Start the clientLogin.
			//ChannelFuture f = null;
			try {
				f = b.connect(HOST, PORT).sync();
									
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO 이후 처리하도록 구성...
			// Wait until the connection is closed.
			try {
				f.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			// Shut down the event loop to terminate all threads.
			group.shutdownGracefully();
		}
	}
	
	public void sendMessage(String msg){
		// TODO  다중 처리시 문제가 없는지 확인 요망.
		if(f.channel().isWritable()){
			ByteBuf firstMessage;
			firstMessage = Unpooled.buffer(msg.getBytes().length);
	        firstMessage.writeBytes(msg.getBytes());
	        
			f.channel().writeAndFlush(firstMessage);
			logger.debug("sendMessage() : " + msg);
		}
	}
	
} // class end
