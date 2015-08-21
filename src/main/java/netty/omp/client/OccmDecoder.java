package netty.omp.client;

import java.util.List;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class OccmDecoder extends ByteToMessageDecoder {
	
	static final Logger logger = Logger.getLogger(OccmDecoder.class);
	
	int sizeHeader = 17;
	int posbodyLength = 11;
	// 형식
	// [<MSGSIZE>( 100)][<Command>(UNMASKALARM)][<RetVal>(IGW:C-IGW:IGW1)][<SendAPU>(0)][<System>(GTEST01)][<OMPName>(OMP3)]
	
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
    	
    	// TODO 파일일 경우의 처리 decode를 변경 처리 한다.
    	
//		System.out.println("OccmDecoder ====================================================================================");
//		System.out.println("OccmDecoder : in " + in.toString());
//		System.out.println("OccmDecoder : in.msg :" + in.toString(Charset.forName("UTF-8")));
    	
		
		while(in.readableBytes() >= sizeHeader){
			// TODO Util로 만들기
	    	byte[] bytesLenght = new byte[4];
			in.getBytes(in.readerIndex() + posbodyLength, bytesLenght, 0, 4);
			int bodyLength = Integer.parseInt((new String(bytesLenght)).trim());
			
			int messageLength = sizeHeader + bodyLength;
			
			//System.out.println("OccmDecoder : in.readableBytes : " + in.readableBytes() + " , body-len : " + bodyLength + " , message-len : " + messageLength);
			if(in.readableBytes() < messageLength ){
				//System.out.println("OccmDecoder : in.readableBytes() < messageLength ");
				return;
			}
	    	//System.out.println("OccmDecoder : length : " + bodyLength + ", in :  " + in.toString());
	    	
	    	ByteBuf readBytes =  in.readBytes(messageLength);
	    	//System.out.println("OccmDecoder : Read Byte : " + readBytes.readableBytes() + ", in : " + readBytes.toString());

	    	out.add(readBytes);
		}
    }
}
