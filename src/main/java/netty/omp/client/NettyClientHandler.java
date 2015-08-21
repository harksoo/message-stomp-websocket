package netty.omp.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyClientHandler  extends SimpleChannelInboundHandler<Object> {

	long startTime = -1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (startTime < 0) {
            startTime = System.currentTimeMillis();
        }
        println("Connected to: " + ctx.channel().remoteAddress());
        
        // TODO 전송처리
        
        String msg = "[<MSGSIZE>( 184)][<Command>(LOGINUSER)][<Userid>(superusr)][<Passwd>(1234)][<Ip>(10.10.105.127)][<Hostname>(harksoo-pc)][<Mac>(00:30:67:DD:D5:E1)][<ClientDate>(15073115)][<LoginType>(GUI)][<PID>(2340)]"; 
        
        final ChannelFuture f = ctx.writeAndFlush(msg); // (3)
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                //ctx.close();
            }
        }); // (4)
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Discard received data
    	println("Received Data : " + msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }

//        IdleStateEvent e = (IdleStateEvent) evt;
//        if (e.state() == IdleState.READER_IDLE) {
//            // The connection was OK but there was no traffic for last period.
//            println("Disconnecting due to no inbound traffic");
//            ctx.close();
//        }
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        println("Disconnected from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        println("Sleeping for: " + NettyClient.RECONNECT_DELAY + 's');

        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                println("Reconnecting to: " + NettyClient.HOST + ':' + NettyClient.PORT);
                NettyClient.connect(NettyClient.configureBootstrap(new Bootstrap(), loop));
            }
        }, NettyClient.RECONNECT_DELAY, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    void println(String msg) {
        if (startTime < 0) {
            System.err.format("[SERVER IS DOWN] %s%n", msg);
        } else {
            System.err.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
        }
    }
}
