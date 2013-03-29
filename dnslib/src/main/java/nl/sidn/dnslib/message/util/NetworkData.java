package nl.sidn.dnslib.message.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class NetworkData {
	
	private byte[] buf;

	private int index = 0;
	private int markedIndex = 0;;
	
	private int byte1 = 0;
	private int byte2 = 0;
	private int byte3 = 0;
	private int byte4 = 0;
	
	//write part
	private ByteArrayOutputStream backing;
	private DataOutput writeBuffer;
	private int writerIndex;
	
	public NetworkData(int size){
		backing = new ByteArrayOutputStream(size);
		writeBuffer = new DataOutputStream(backing) ;
	}
	
	public NetworkData(){
		this(4096);
	}
	
	public NetworkData(byte[] data){
		this.buf = data;
		index = 0;
	}
	
	public long readUnsignedInt(){
		
		byte1 = (0xFF & buf[index]);
		byte2 = (0xFF & buf[index+1]);
		byte3 = (0xFF & buf[index+2]);
		byte4 = (0xFF & buf[index+3]);
        index = index+4;
        
        return  ((long) (byte1 << 24
	                | byte2 << 16
                    | byte3 << 8
                    | byte4))
                   & 0xFFFFFFFFL;
	}
	

	public short readUnsignedByte(){
		byte1 = (0xFF & buf[index]);
		index++;
		return (short)byte1;
	}
	
	
	public char readUnsignedChar(){
		byte1 = (0xFF & buf[index]);
		byte2 = (0xFF & buf[index+1]);
		index = index+2;
		return (char) (byte1 << 8 | byte2);
	}
	
	
	public void readBytes(byte[] destination){
		System.arraycopy(buf, index, destination, 0, destination.length);
		index = index + destination.length;
	}
	
	
	public void writeChar(int c){
		try {
			writeBuffer.writeChar(c);
			writerIndex+=2;
		} catch (IOException e) {
			throw new RuntimeException("Error while writing data", e);
		}
	}
	
	public void writeByte(int b){
		try {
			writeBuffer.write(b);
			writerIndex++;
		} catch (IOException e) {
			throw new RuntimeException("Error while writing data", e);
		}
	}
	
	public void writeBytes(byte[] b){
		try {
			writeBuffer.write(b);
			writerIndex = writerIndex + b.length;
		} catch (IOException e) {
			throw new RuntimeException("Error while writing data", e);
		}
	}
	
	public void writeInt(long i){
		try {
			writeBuffer.writeInt((int)i);
			writerIndex+=4;
		} catch (IOException e) {
			throw new RuntimeException("Error while writing data", e);
		}
	}
	
	public byte[] write(){
		byte[] data = backing.toByteArray();
		return Arrays.copyOf(data, writerIndex);
	}
	
	public int readableBytes(){
		if(buf != null){
			return buf.length;
		}
		
		return 0;
	}
	
	public int writableBytes(){
		return writerIndex;
	}

	public int getReaderIndex() {
		return index;
	}
	
	public void setReaderIndex(int index) {
		this.index = index;
	}

	public int getWriterIndex() {
		return writerIndex;
	}
	
	public void markReaderIndex(){
		markedIndex = index;
	}

	public void resetReaderIndex(){
		index = markedIndex;
	}
	
	

}
