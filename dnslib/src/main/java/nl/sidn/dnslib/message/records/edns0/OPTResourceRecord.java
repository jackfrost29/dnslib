package nl.sidn.dnslib.message.records.edns0;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.ResourceRecordClass;
import nl.sidn.dnslib.types.ResourceRecordType;

import org.apache.log4j.Logger;

/**
 * 
 * EDNS Header Flags (16 bits)

	Registration Procedures
	Standards Action
	Reference
	[RFC-ietf-dnsext-rfc2671bis-edns0-10]
	Bit 	Flag 	Description 	Reference 
	Bit 0	DO	DNSSEC answer OK	[RFC4035][RFC3225]
	Bit 1-15		Reserved	
 *
 */
public class OPTResourceRecord extends AbstractResourceRecord {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(OPTResourceRecord.class);
	
	private static final char DNSSEC_DO_BIT_MASK = 0x8000; //1000 0000 0000 0000
	
	private String name = ".";
	private ResourceRecordType type = ResourceRecordType.OPT;
	private char udpPlayloadSize = 4096;

	private char rdLeng = 0;
	
	private short rcode;
	private short version;
	private char flags;
	
	private boolean dnssecDo;
	
	
	@Override
	public void decode(NetworkData buffer) {
		//name
		name = DNSStringUtil.readName(buffer);
		
		char type = buffer.readUnsignedChar();
		setType(ResourceRecordType.fromValue(type));
		
		udpPlayloadSize = buffer.readUnsignedChar();
		
		rcode = buffer.readUnsignedByte();
		
		version = buffer.readUnsignedByte();
		
		flags = buffer.readUnsignedChar();
		
		dnssecDo = (flags & DNSSEC_DO_BIT_MASK) == DNSSEC_DO_BIT_MASK;
		
		rdLeng = buffer.readUnsignedChar();
	}

	@Override
	public void encode(NetworkData buffer) {
		
		LOGGER.debug("encode");
		
		//write the name 
		buffer.writeByte(0);
		
		//write the opt type
		buffer.writeChar(type.getValue());
		
		//write the supported udp size
		buffer.writeChar(udpPlayloadSize);
	
		//write extended rcode
		buffer.writeByte(0x0); 
		
		//write version
		buffer.writeByte(0x0); 
		
		//default all flags off
		char flags = 0x0;
		
		//dnssec enabled, signal with do bit is on
		flags = (char)(flags | DNSSEC_DO_BIT_MASK);
		
		//write all the flags
		buffer.writeChar(flags); 

		//write the length of the rdata section
		buffer.writeChar(rdLeng); 
	}


	@Override
	public String toString() {
		return "OPTResourceRecord [name=" + name + ", type=" + type
				+ ", udpPlayloadSize=" + (int)udpPlayloadSize + ", rdLeng=" + (int)rdLeng
				+ ", doBit=" + dnssecDo + "]";
	}

	public boolean getDnssecDo() {
		return dnssecDo;
	}
	
	public void setDnssecDo(boolean dnssecDo) {
		this.dnssecDo = dnssecDo;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getUdpPlayloadSize() {
		return udpPlayloadSize;
	}

	public void setUdpPlayloadSize(char udpPlayloadSize) {
		this.udpPlayloadSize = udpPlayloadSize;
	}

	public short getRcode() {
		return rcode;
	}

	public void setRcode(short rcode) {
		this.rcode = rcode;
	}

	public short getVersion() {
		return version;
	}

	public void setVersion(short version) {
		this.version = version;
	}

	public char getFlags() {
		return flags;
	}

	public void setFlags(char flags) {
		this.flags = flags;
	}

	@Override
	public String toZone(int maxLength) {
		return "";
	}

}
