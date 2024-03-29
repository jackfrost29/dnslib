package nl.sidn.dnslib.message;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.MessageType;
import nl.sidn.dnslib.types.OpcodeType;
import nl.sidn.dnslib.types.RcodeType;

public class Header {
	
	/*
	 
	                                1  1  1  1  1  1
      0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                      ID                       |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |QR|   Opcode  |AA|TC|RD|RA|   Z    |   RCODE   |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    QDCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ANCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    NSCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
    |                    ARCOUNT                    |
    +--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
	 
	 where:

		ID              A 16 bit identifier assigned by the program that
		                generates any kind of query.  This identifier is copied
		                the corresponding reply and can be used by the requester
		                to match up replies to outstanding queries.
		
		QR              A one bit field that specifies whether this message is a
		                query (0), or a response (1).
		
		OPCODE          A four bit field that specifies kind of query in this
		                message.  This value is set by the originator of a query
		                and copied into the response.  
		                
		AA              Authoritative Answer - this bit is valid in responses,
		                and specifies that the responding name server is an
		                authority for the domain name in question section.
		
		                Note that the contents of the answer section may have
		                multiple owner names because of aliases.  The AA bit		
		                corresponds to the name which matches the query name, or
		                the first owner name in the answer section.
		
		TC              TrunCation - specifies that this message was truncated
		                due to length greater than that permitted on the
		                transmission channel.
		
		RD              Recursion Desired - this bit may be set in a query and
		                is copied into the response.  If RD is set, it directs
		                the name server to pursue the query recursively.
		                Recursive query support is optional.
		
		RA              Recursion Available - this be is set or cleared in a
		                response, and denotes whether recursive query support is
		                available in the name server.
		
		Z               Reserved for future use.  Must be zero in all queries
		                and responses.
		
		RCODE           Response code - this 4 bit field is set as part of
		                responses.  The values have the following
			
		QDCOUNT         an unsigned 16 bit integer specifying the number of
		                entries in the question section.
		
		ANCOUNT         an unsigned 16 bit integer specifying the number of
		                resource records in the answer section.
		
		NSCOUNT         an unsigned 16 bit integer specifying the number of name
		                server resource records in the authority records
		                section.
		
		ARCOUNT         an unsigned 16 bit integer specifying the number of
		                resource records in the additional records section.
	 
	 
	 */
	
	private static int QR_QUERY_BIT_MASK = 32768;				// 1000 0000 0000 0000
	private static int OPCODE_STANDARD_QUERY_BIT_MASK = 61439;	// 1110 1111 1111 1111
	private static int OPCODE_INVERSE_QUERY_BIT_MASK = 2048 ;   // 0000 1000 0000 0000
	private static int OPCODE_STATUS_REQUEST_BIT_MASK = 4096 ;  // 0001 0000 0000 0000
	private static int AA_BIT_MASK = 1024 ; 				    // 0000 0100 0000 0000
	private static int TC_BIT_MASK = 512 ;  					// 0000 0010 0000 0000
	private static int RD_BIT_MASK = 256 ; 						// 0000 0001 0000 0000
	private static int RA_BIT_MASK = 128 ;  					// 0000 0000 1000 0000
	private static int Z_BIT_MASK = 64 ;  						// 0000 0000 0100 0000
	private static int AD_BIT_MASK = 32 ;  						// 0000 0000 0010 0000
	private static int CD_BIT_MASK = 16 ; 						// 0000 0000 0001 0000
	
	private static int RCODE_NO_ERROR_BIT_MASK = 65534;			// 1111 1111 1111 1110
	private static int RCODE_FORMAT_ERROR_BIT_MASK = 1;			// 0000 0000 0000 0001
	private static int RCODE_SERVER_FAILURE_BIT_MASK = 2;		// 0000 0000 0000 0010
	private static int RCODE_NAME_ERROR_BIT_MASK = 3;			// 0000 0000 0000 0011
	private static int RCODE_NOT_IMPLEMENTED_BIT_MASK = 4;		// 0000 0000 0000 0100
	private static int RCODE_REFUSED_BIT_MASK = 5;				// 0000 0000 0000 0101

	private char id;
	private MessageType qr = MessageType.QUERY;
	private OpcodeType OpCode;
	private boolean aa;
	private boolean tc;
	private boolean rd;
	private boolean ra;
	private boolean z;
	private boolean ad;
	private boolean cd;
	private RcodeType rcode;
	private char qdCount;
	private char anCount;
	private char nsCount;
	private char arCount;
	
	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
	}
	
	public MessageType getQr() {
		return qr;
	}

	public void setQr(MessageType qr) {
		this.qr = qr;
	}

	public OpcodeType getOpCode() {
		return OpCode;
	}

	public void setOpCode(OpcodeType opCode) {
		OpCode = opCode;
	}

	public boolean isAa() {
		return aa;
	}

	public void setAa(boolean aa) {
		this.aa = aa;
	}

	public boolean isTc() {
		return tc;
	}

	public void setTc(boolean tc) {
		this.tc = tc;
	}

	public boolean isRd() {
		return rd;
	}

	public void setRd(boolean rd) {
		this.rd = rd;
	}

	public boolean isRa() {
		return ra;
	}

	public void setRa(boolean ra) {
		this.ra = ra;
	}

	public boolean isZ() {
		return z;
	}

	public void setZ(boolean z) {
		this.z = z;
	}

	public boolean isAd() {
		return ad;
	}

	public void setAd(boolean ad) {
		this.ad = ad;
	}

	public boolean isCd() {
		return cd;
	}

	public void setCd(boolean cd) {
		this.cd = cd;
	}

	public RcodeType getRcode() {
		return rcode;
	}

	public void setRcode(RcodeType rcode) {
		this.rcode = rcode;
	}
	

	public char getQdCount() {
		return qdCount;
	}

	public void setQdCount(char qdCount) {
		this.qdCount = qdCount;
	}

	public char getAnCount() {
		return anCount;
	}

	public void setAnCount(char anCount) {
		this.anCount = anCount;
	}

	public char getNsCount() {
		return nsCount;
	}

	public void setNsCount(char nsCount) {
		this.nsCount = nsCount;
	}

	public char getArCount() {
		return arCount;
	}

	public void setArCount(char arCount) {
		this.arCount = arCount;
	}
	

	public void decode(NetworkData buffer) {
		//get the message id
		setId(buffer.readUnsignedChar());
		//decode the flags (next 16 bits)
		decodeFlags(buffer);
				
		//read the counters for the RR's
		setQdCount(buffer.readUnsignedChar());
		setAnCount(buffer.readUnsignedChar());
		setNsCount(buffer.readUnsignedChar());
		setArCount(buffer.readUnsignedChar());
		
	}
	
	private void decodeFlags(NetworkData buffer ){
		
		char flags = buffer.readUnsignedChar();

		if((flags & QR_QUERY_BIT_MASK) == QR_QUERY_BIT_MASK){
			//the message is a response
			setQr(MessageType.RESPONSE);
		}else{
			//the message is a query
			setQr(MessageType.QUERY);			
		}
		
		/* 
		 * OPCODE A four bit field that specifies kind of query in this
         * message.  This value is set by the originator of a query
         * and copied into the response.  The values are:
         * 
         *      0               a standard query (QUERY)
         *      1               an inverse query (IQUERY)
         *      2               a server status request (STATUS)
         *      3-15            reserved for future use
		 * 
		 */
		
		if((flags | OPCODE_STANDARD_QUERY_BIT_MASK) == OPCODE_STANDARD_QUERY_BIT_MASK){
			setOpCode(OpcodeType.STANDARD);
		}else if((flags & OPCODE_INVERSE_QUERY_BIT_MASK) == OPCODE_INVERSE_QUERY_BIT_MASK){
			setOpCode(OpcodeType.INVERSE);
		}else if((flags & OPCODE_STATUS_REQUEST_BIT_MASK) == OPCODE_STATUS_REQUEST_BIT_MASK){
			setOpCode(OpcodeType.INVERSE);
		}
		
		/* AA  Authoritative Answer - this bit is valid in responses,
         * and specifies that the responding name server is an
         * authority for the domain name in question section.
         */
		
		setAa((flags & AA_BIT_MASK) == AA_BIT_MASK);
		
		/* TC TrunCation - specifies that this message was truncated
           due to length greater than that permitted on the
           transmission channel.
         */
		setTc((flags & TC_BIT_MASK) == TC_BIT_MASK);
		
		/* RD  Recursion Desired - this bit may be set in a query and
           is copied into the response.  If RD is set, it directs
           the name server to pursue the query recursively.
           Recursive query support is optional.
         */
		setRd((flags & RD_BIT_MASK) == RD_BIT_MASK);
		
		/* RA Recursion Available - this be is set or cleared in a
           response, and denotes whether recursive query support is
           available in the name server.
         */
		setRa((flags & RA_BIT_MASK) == RA_BIT_MASK);
		
		/* Z Reserved for future use.  Must be zero in all queries
           and responses.
        */
		setZ((flags & Z_BIT_MASK) == Z_BIT_MASK);
		
		/*
		 	DNSSEC allocates two new bits in the DNS message header: the CD
		   (Checking Disabled) bit and the AD (Authentic Data) bit.  The CD bit
		   is controlled by resolvers; a security-aware name server MUST copy
		   the CD bit from a query into the corresponding response.  The AD bit
		   is controlled by name servers; a security-aware name server MUST
		   ignore the setting of the AD bit in queries.  See Sections 3.1.6,
		   3.2.2, 3.2.3, 4, and 4.9 for details on the behavior of these bits.
		 */
		
		/* AD Authentic data (DNSSEC) */
		setAd((flags & AD_BIT_MASK) == AD_BIT_MASK);
		
		/* CD Checking Disabled - non-authenticated data is acceptable (DNSSEC) */
		setCd((flags & CD_BIT_MASK) == CD_BIT_MASK);
		
		/* RCODE Response code - this 4 bit field is set as part of
           responses.  The values have the following
           interpretation:

        	0               No error condition

        	1               Format error - The name server was
                        	unable to interpret the query.

        	2               Server failure - The name server was
                        	unable to process this query due to a
                        	problem with the name server.

        	3               Name Error - Meaningful only for
                        	responses from an authoritative name
                        	server, this code signifies that the
                        	domain name referenced in the query does
                        	not exist.

        	4               Not Implemented - The name server does
                        	not support the requested kind of query.

        	5               Refused - The name server refuses to
                        	perform the specified operation for
                        	policy reasons.  For example, a name
                        	server may not wish to provide the
                        	information to the particular requester,
                        	or a name server may not wish to perform
                        	a particular operation (e.g., zone
                        	transfer) for particular data.

        	6-15            Reserved for future use.
        */
		
		if((flags | RCODE_NO_ERROR_BIT_MASK) == RCODE_NO_ERROR_BIT_MASK){
			setRcode(RcodeType.NO_ERROR);
		}else if((flags & RCODE_NAME_ERROR_BIT_MASK) == RCODE_NAME_ERROR_BIT_MASK){
			setRcode(RcodeType.NXDOMAIN);
		}else if((flags & RCODE_FORMAT_ERROR_BIT_MASK) == RCODE_FORMAT_ERROR_BIT_MASK){
			setRcode(RcodeType.FORMAT_ERROR);
		}else if((flags & RCODE_SERVER_FAILURE_BIT_MASK) == RCODE_SERVER_FAILURE_BIT_MASK){
			setRcode(RcodeType.SERVER_FAILURE);
		}else if((flags & RCODE_NOT_IMPLEMENTED_BIT_MASK) == RCODE_NOT_IMPLEMENTED_BIT_MASK){
			setRcode(RcodeType.NOT_IMPLEMENTED);
		}else if((flags & RCODE_REFUSED_BIT_MASK) == RCODE_REFUSED_BIT_MASK){
			setRcode(RcodeType.REFUSED);
		}else{
			setRcode(RcodeType.RESERVED);
		}
	
		
	}

	
	public void encode(NetworkData buffer) {
		
		//write unique 16bit id for the packet
		buffer.writeChar(getId()); 
		
		/* create a bitmask for the header status flags.
		 * start with all flags to zero and flip the
		 * bits where apropriate.
		 * 
		 * 0000 0000 0000 0000  16 bit mask
		 */
		char flags = 0x0; 
		
		if(qr == MessageType.RESPONSE){
			//flip the response flag
			flags  =  (char) (flags | 0x8000); //1000 0000 0000 0000
		}
		
		if(OpCode == OpcodeType.INVERSE){
			flags  =  (char) (flags | 0x800); //0000 1000 0000 0000
		}else if(OpCode == OpcodeType.STATUS){
			flags = (char) (flags | 0x1000); //0001 0000 0000 0000
		}
		
		if(aa){
			flags = (char) (flags | 0x400); //0000 0100 0000 0000
		}
		
		if(tc){
			flags = (char) (flags | 0x200); //0000 0010 0000 0000
		}
		
		if(rd){
			flags = (char) (flags | 0x100); //0000 0001 0000 0000
		}
		
		if(ra){
			flags = (char) (flags | 0x80); //0000 0000 1000 0000
		}
		
		if(z){
			flags = (char) (flags | 0x40); //0000 0000 0100 0000
		}
		
		if(ad){
			flags = (char) (flags | 0x20); //0000 0000 0010 0000
		}
		
		if(cd){
			flags = (char) (flags | 0x10); //0000 0000 0001 0000
		}
		//determine the correct rcode bitmask
		if(rcode == RcodeType.FORMAT_ERROR){
			flags = (char) (flags | 0x1); //0000 0000 0000 0001
		}else if(rcode == RcodeType.SERVER_FAILURE){
			flags = (char) (flags | 0x2); //0000 0000 0000 0010
		}else if(rcode == RcodeType.NXDOMAIN){
			flags = (char) (flags | 0x3); //0000 0000 0000 0011
		}else if(rcode == RcodeType.NOT_IMPLEMENTED){
			flags = (char) (flags | 0x4); //0000 0000 0000 0100
		}else if(rcode == RcodeType.REFUSED){
			flags = (char) (flags | 0x5); //0000 0000 0000 0101
		}
			
		//write the flags
		buffer.writeChar(flags);
		//question count
		buffer.writeChar((char) getQdCount());
		//an count
		buffer.writeChar((char) getAnCount());
		//ns count
		buffer.writeChar((char) getNsCount());
		//ar count
		buffer.writeChar((char) getArCount());
		
	}

	@Override
	public String toString() {
		return "Header [id=" + (int)id + ", qr=" + qr + ", OpCode=" + OpCode
				+ ", aa=" + aa + ", tc=" + tc + ", rd=" + rd + ", ra=" + ra
				+ ", z=" + z + ", ad=" + ad + ", cd=" + cd + ", rcode=" + rcode
				+ ", qdCount=" + (int)qdCount + ", anCount=" + (int)anCount
				+ ", nsCount=" + (int)nsCount + ", arCount=" + (int)arCount + "]";
	}


	public static Header createResponseHeader(char id){
		Header hdr = new Header();
		hdr.setId(id);
		hdr.setQr(MessageType.RESPONSE);
		return hdr;
	}

	public String toZone() {
		return "flags: aa:" + aa + ", tc:" + tc + ", rd:" + rd + ", ra:" + ra
				+ ", ad:" + ad + ", cd:" + cd + ", rcode:" + rcode
				+ ", qdCount:" + (int)qdCount + ", anCount:" + (int)anCount
				+ ", nsCount:" + (int)nsCount + ", arCount:" + (int)arCount;
	}
	
	public JsonObject toJSon(){
		return Json.createObjectBuilder().
			add("id", id).
			add("qr", qr.getValue()).
			add("opcode", OpCode.getValue()).	
			add("flags", Json.createObjectBuilder().
				add("aa", aa).
				add("tc", tc).
				add("rd", rd).
				add("ra", ra).
				add("ad", ad).
				add("cd", cd)).
			add("rcode", rcode.name()).
			add("qdCount", qdCount).
			add("anCount", anCount).
			add("nsCount", nsCount).
			add("arCount", arCount).
			build();
	}

	public void toJSon(JsonGenerator g) {
		try {
			g.writeNumberField("id", (int)id);
			g.writeNumberField("qr", qr.getValue());
			g.writeNumberField("opcode", (int)OpCode.getValue());
			g.writeObjectFieldStart("flags");
			g.writeBooleanField("aa", aa);
			g.writeBooleanField("tc", tc);
			g.writeBooleanField("rd", rd);
			g.writeBooleanField("ra", ra);
			g.writeBooleanField("ad", ad);
			g.writeBooleanField("cd", cd);
			g.writeEndObject();
			g.writeNumberField("rcode", (int)rcode.getValue());
			g.writeNumberField("qdCount", qdCount);
			g.writeNumberField("anCount", anCount);
			g.writeNumberField("nsCount", nsCount);
			g.writeNumberField("arCount", arCount);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
