package nl.sidn.dnslib.message.records.dnssec;

import java.io.IOException;
import java.security.PublicKey;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerator;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.AlgorithmType;
import nl.sidn.dnslib.util.KeyUtil;

public class DNSKEYResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	/*
	   The RDATA for a DNSKEY RR consists of a 2 octet Flags Field, a 1
	   octet Protocol Field, a 1 octet Algorithm Field, and the Public Key
	   Field.
	
	                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3
	    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |              Flags            |    Protocol   |   Algorithm   |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   /                                                               /
	   /                            Public Key                         /
	   /                                                               /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 
	 */

	
	private char flags;
	private short protocol;
	private AlgorithmType algorithm;
	private PublicKey publicKey;
	private byte[] keydata;

	private int keytag;
	private boolean isZoneKey;
	private boolean isSepKey;
	
	
	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
		
		flags = buffer.readUnsignedChar();
		
		protocol = buffer.readUnsignedByte();
		
		short algorithm = buffer.readUnsignedByte();
		this.algorithm = AlgorithmType.fromValue(algorithm);
		
		char keysize = (char) (rdLength - 4); //4 is length flags+proto+alg 
		keydata = new byte[keysize];
		buffer.readBytes(keydata);
	
		publicKey = KeyUtil.createRSAPublicKey(keydata);
		
		keytag =  KeyUtil.createKeyTag(rdata, algorithm);
		
		isZoneKey = KeyUtil.isZoneKey(this);
		
		isSepKey = KeyUtil.isSepKey(this);
	}
	
	public boolean isValid(){

		if(protocol != 3){
			//invalid key, skip checking this rrsig
			return false;
		}
		//The Flag field MUST be represented as an unsigned decimal integer.
		// Given the currently defined flags, the possible values are: 0, 256,
		// and 257.
		if(flags!= 0 && flags != 256 && flags != 257 ){
			return false;
		}
			
		return true;
	
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		
		buffer.writeChar(flags);
		
		buffer.writeByte(protocol);
		
		buffer.writeByte(algorithm.getValue());
		
		buffer.writeBytes(keydata);
	}

	@Override
	public String toString() {
		return "DNSKEYResourceRecord [flags=" + (int) flags + ", protocol="
				+ protocol + ", algorithm=" + algorithm + ", rdLength=" + (int)rdLength
				+ ", isZoneKey=" + isZoneKey + ", isSepKey=" + isSepKey + ", keytag=" + keytag + "]";
	}
	
	

	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + " " + (int)flags + " " + protocol + " " + algorithm.getValue() +
				"(\n\t\t\t\t\t\t" + new Base64(36, "\n\t\t\t\t\t\t".getBytes()).encodeAsString(keydata) + " )";
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("flags", (int)flags)).
				add("protocol", protocol).
				add("algorithm", algorithm.name()).
				add("zone-key", isZoneKey).
				add("sep-key", isSepKey).
				add("keytag", keytag).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeNumberField("flags", (int)flags);
			g.writeObjectField("protocol", protocol);
			g.writeObjectField("algorithm", algorithm.name());
			g.writeBooleanField("zone-key", isZoneKey);
			g.writeBooleanField("sep-key", isSepKey);
			g.writeNumberField("keytag", keytag);
			
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public char getFlags() {
		return flags;
	}

	public void setFlags(char flags) {
		this.flags = flags;
	}

	public short getProtocol() {
		return protocol;
	}

	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

	public AlgorithmType getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}


	
	
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	

	public byte[] getKeydata() {
		return keydata;
	}

	public void setKeydata(byte[] keydata) {
		this.keydata = keydata;
	}
	


	public int getKeytag() {
		return keytag;
	}

	public void setKeytag(int keytag) {
		this.keytag = keytag;
	}

	public boolean isZoneKey() {
		return isZoneKey;
	}

	public boolean isSepKey() {
		return isSepKey;
	}


	
}
