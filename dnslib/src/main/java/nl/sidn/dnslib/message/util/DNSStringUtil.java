package nl.sidn.dnslib.message.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import nl.sidn.dnslib.exception.DnsDecodeException;
import nl.sidn.dnslib.exception.DnsEncodeException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * DNS Label Types

	Registration Procedures
	IESG Approval
	Reference
	[RFC-ietf-dnsext-rfc2671bis-edns0-10]
	Note
	IETF standards action required to allocate new types
	The top 2 bits of the first byte of an DNS label indicates the type of label.
	Registration of further Extended Label Types is closed per [RFC-ietf-dnsext-rfc2671bis-edns0-10].
	
	Value 	Type 	Status 	Reference 
	0 0	Normal label lower 6 bits is the length of the label	Standard	[RFC1035]
	1 1	Compressed label the lower 6 bits and the 8 bits from next octet form a pointer to the compression target.	Standard	[RFC1035]
	0 1	Extended label type the lower 6 bits of this type (section 3) indicate the type of label in use	Proposed	[RFC-ietf-dnsext-rfc2671bis-edns0-10]
	0 1 0 0 0 0 0 1	Binary Label	Experimental not recommended	[RFC3364][RFC3363][RFC2673]
	0 1 1 1 1 1 1 1	Reserved for future expansion.	Proposed	[RFC-ietf-dnsext-rfc2671bis-edns0-10]
	1 0	Unallocated		

 *
 */
public class DNSStringUtil {
	
	//max length of a rfc1035 character-string (excluding length byte)
	private static int MAX_CHARACTER_STRING_LENGTH = 255;
	
	/*
	 
	 	4.1.4. Message compression

		In order to reduce the size of messages, the domain system utilizes a
		compression scheme which eliminates the repetition of domain names in a
		message.  In this scheme, an entire domain name or a list of labels at
		the end of a domain name is replaced with a pointer to a prior occurance
		of the same name.
		
		The pointer takes the form of a two octet sequence:
		
		    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		    | 1  1|                OFFSET                   |
		    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
		
		The first two bits are ones.  This allows a pointer to be distinguished
		from a label, since the label must begin with two zero bits because
		labels are restricted to 63 octets or less.  (The 10 and 01 combinations
		are reserved for future use.)  The OFFSET field specifies an offset from
		the start of the message (i.e., the first octet of the ID field in the
		domain header).  A zero offset specifies the first byte of the ID field,
		etc.
		
		The compression scheme allows a domain name in a message to be
		represented as either:
		
		   - a sequence of labels ending in a zero octet
		
		   - a pointer
		
		   - a sequence of labels ending with a pointer
		
		Pointers can only be used for occurances of a domain name where the
		format is not class specific.  If this were not the case, a name server
		or resolver would be required to know the format of all RRs it handled.
		As yet, there are no such cases, but they may occur in future RDATA
		formats.
	 
	 
	 */
	
	private static final Logger LOGGER = Logger.getLogger(DNSStringUtil.class);
	
	private static byte UNCOMPRESSED_NAME_BIT_MASK = (byte)0x3f;         //0011 1111
	private static byte COMPRESSED_NAME_BIT_MASK = (byte)0xc0;   		//1100 0000
	

	public static boolean isUncompressedName(byte namePrefix){
		return (namePrefix | UNCOMPRESSED_NAME_BIT_MASK) == UNCOMPRESSED_NAME_BIT_MASK;
	}
	
	public static boolean isCompressedName(byte namePrefix){
		return (namePrefix & COMPRESSED_NAME_BIT_MASK) == COMPRESSED_NAME_BIT_MASK;
	}
	
	
	public static String readName(NetworkData buffer){
		short length = buffer.readUnsignedByte();

		if(length == 0){
			/* zero lentgh label means "." root */ 
			return ".";
		}
		if(isUncompressedName((byte)length)){
			return readUncompressedName(length, buffer) + ".";
		}else if(isCompressedName((byte)length)){
			return readCompressedName(buffer) + ".";
		}
		
		//TODO: return not support error
		
		return null;
	}
	

	public static String readUncompressedName(short length, NetworkData buffer){
		StringBuffer qnameBuffer = new StringBuffer();
		
		//read the length of the first label
		
		while(length > 0){
			if(qnameBuffer.length() > 0){
				//add the "." between labels
				qnameBuffer.append(".");
			}
			//read the label
			byte[] bytes =new byte[length];
	        buffer.readBytes(bytes);
			qnameBuffer.append(new String(bytes));
			
			//read the length of the next label
			length = buffer.readUnsignedByte();
			
			//check if the last label is a pointer
			if(isCompressedName((byte)length)){
				qnameBuffer.append(".");
				qnameBuffer.append(readCompressedName(buffer));
				//the pointer is the last label, quit the loop
				break;
			}
		}
		
		return qnameBuffer.toString();
	}
	
	
	public static String readCompressedName(NetworkData buffer) {

		// go back one byte
		buffer.setReaderIndex(buffer.getReaderIndex() - 1);

		//read 16 bits
		char offset = buffer.readUnsignedChar();

		offset = (char) (offset ^ (1 << 14)); // flip bit 14 to 0
		offset = (char) (offset ^ (1 << 15)); // flip bit 15 to 0

		// save current location in the stream
		int currentPosition = buffer.getReaderIndex();
	
		// goto the pointer location in the buffer
		buffer.setReaderIndex(offset);
		
		/* read the uncompressed name at the offset this
		 * name can also end with a compressed part.
		 * this will cause the current method to be called again
		 * which must therefore be reentrant.
		 */
		short length = buffer.readUnsignedByte();
		String qName = readUncompressedName(length, buffer);
		
		//go back to the location after the first pointer
        buffer.setReaderIndex(currentPosition);
		
		return qName;
	}
	
	
	public static void writeName(String name, NetworkData buffer){
		
		//write nameserver string
		String[] labels = StringUtils.split(name, ".");
		for (String label : labels) {
			//write label length
			buffer.writeByte(label.length());	
			buffer.writeBytes(label.getBytes());
		}
				
		//write root with zero byte
		buffer.writeByte(0);
		
	}
	
	public static byte[] writeName(String name){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			//write nameserver string
			String[] labels = StringUtils.split(name, ".");
			for (String label : labels) {
				//write label length
				dos.writeByte(label.length());	
				dos.write(label.getBytes());
			}
					
			//write root with zero byte
			dos.writeByte(0);
		} catch (IOException e) {
			throw new RuntimeException("Error while wrting name", e);
		}
		
		return bos.toByteArray();

	}
	
	
	public static String readCharacterString(NetworkData buffer){
		int length = buffer.readUnsignedByte();
		if(length > MAX_CHARACTER_STRING_LENGTH){
			throw new DnsDecodeException("Illegal character string length (> 255), length = " + length);
		}
		if(length > 0){
			byte[] characterString = new byte[length];
			buffer.readBytes(characterString);
			return new String(characterString);
		}
		
		return "";
		
	}
	
	public static void writeCharacterString(String value, NetworkData buffer){
		byte[] data = value.getBytes();
		if(data.length > MAX_CHARACTER_STRING_LENGTH){
			throw new DnsEncodeException("Illegal character string length (> 255), length = " + data.length);
		}
		if(data.length > 0){
			buffer.writeByte(data.length);
			buffer.writeBytes(data);
		}
	}

}
