package nl.sidn.dnslib.message.records.dnssec;

import java.io.IOException;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.AlgorithmType;
import nl.sidn.dnslib.types.DigestType;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;


public class DSResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(DSResourceRecord.class);
	
	
	/*
	   The RDATA for a DS RR consists of a 2 octet Key Tag field, a 1 octet
	   Algorithm field, a 1 octet Digest Type field, and a Digest field.
	
	                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3
	    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |           Key Tag             |  Algorithm    |  Digest Type  |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   /                                                               /
	   /                            Digest                             /
	   /                                                               /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 */

	private char keytag;
	private AlgorithmType algorithm;
	private DigestType digestType;
	private byte[] digest;
	
	private String hex;
	
	
	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
			
		keytag = buffer.readUnsignedChar();
		
		short alg = buffer.readUnsignedByte();
		algorithm = AlgorithmType.fromValue(alg);
	
		short dt = buffer.readUnsignedByte();
		digestType = DigestType.fromValue(dt);
		
		digest = new byte[rdLength - 4];
		buffer.readBytes(digest);
	
		hex = new String(Hex.encodeHex(digest));
	}

	@Override
	public void encode(NetworkData buffer) {
		
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		
		buffer.writeChar(keytag);
	
		buffer.writeByte(algorithm.getValue());
		
		buffer.writeByte(digestType.getValue());
		
		buffer.writeBytes(digest);
	
	}

	@Override
	public String toString() {
		return "DSResourceRecord [rdLength=" + (int) rdLength + ", keytag=" + (int)keytag
				+ ", algorithm=" + algorithm + ", digestType=" + digestType
				+ ", digest=" + Arrays.toString(digest) + "]";
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("keytag", (int)keytag).
				add("algorithm", algorithm != null?algorithm.name() : "").
				add("digest-type", digestType.name()).
				add("digest", hex)).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeNumberField("flags", (int)keytag);
			g.writeObjectField("algorithm", algorithm != null?algorithm.name() : "");
			g.writeObjectField("digest-type", digestType.name());
			g.writeObjectField("digest", hex);
			
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public char getKeytag() {
		return keytag;
	}

	public void setKeytag(char keytag) {
		this.keytag = keytag;
	}

	public AlgorithmType getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}

	public DigestType getDigestType() {
		return digestType;
	}

	public void setDigestType(DigestType digestType) {
		this.digestType = digestType;
	}

	public byte[] getDigest() {
		return digest;
	}

	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	public String getReadableHash() {
		return hex;
	}
	
	public void setReadableHash(String hex) {
		this.hex = hex;
	}

	
	
	

}
