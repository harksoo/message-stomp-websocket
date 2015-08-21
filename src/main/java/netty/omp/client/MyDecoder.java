package netty.omp.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MyDecoder extends ByteToMessageDecoder {
	
	int sizeHeader = 17;
	int sizeRead = 0;
	
	ByteBuf message = null;
	boolean isBody = false;
	
	
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
    	
    	// TODO 파일일 경우 처리를 해야한다.
//    	if (in.readableBytes() < sizeHeader) {
//            return;
//        }
    	
    	byte[] bytes1 = new byte[in.readableBytes()];
		in.getBytes(0, bytes1);
		String s1 = new String(bytes1);
		System.out.println("decoder ====================================================================================");
		System.out.println("decode : in : msg :" + s1);
    	
		while(in.readableBytes() >= sizeHeader){
			// 확인처리 하기
	    	byte[] bytesLenght = new byte[4];
			in.getBytes(11, bytesLenght, 0, 4);
			int bodyLength = Integer.parseInt((new String(bytesLenght)).trim());
			
			System.out.println("decode : header length 17, body-length : " + bodyLength + ", total : " + (17 + bodyLength));
			System.out.println("decode : body : readableBytes : " + in.readableBytes());
			if(in.readableBytes() < sizeHeader + bodyLength ){
				System.out.println("decode : in.readableBytes() < bodyLength : msg :");
				//message = in;
				return;
			}
			
	    	byte[] bytes = new byte[sizeHeader + bodyLength];
	    	in.getBytes(0, bytes, 0, sizeHeader + bodyLength);
	    	String s = new String(bytes);
	    	System.out.println("decode : length : " + bodyLength + " ,full msg :  " + s);
	    	
	    	//out.add(in.readBytes(in.readableBytes()));
	    	out.add(in.readBytes(sizeHeader + bodyLength));
		}
    }
}
