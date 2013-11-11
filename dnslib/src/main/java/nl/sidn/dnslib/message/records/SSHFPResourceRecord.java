package nl.sidn.dnslib.message.records;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.JsonGenerator;

import nl.sidn.dnslib.message.util.NetworkData;

public class SSHFPResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private short algorithm;
	private short fingerprintType;
	private byte[] fingerprint;

	

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
			
		algorithm = buffer.readUnsignedByte();
		
		fingerprintType = buffer.readUnsignedByte();
		
		fingerprint = new byte[rdLength-2];
		buffer.readBytes(fingerprint);
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		//write rdlength
		buffer.writeChar(rdLength);
		
		buffer.writeBytes(rdata);
	}

	public short getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(short algorithm) {
		this.algorithm = algorithm;
	}

	public short getFingerprintType() {
		return fingerprintType;
	}

	public void setFingerprintType(short fingerprintType) {
		this.fingerprintType = fingerprintType;
	}

	public byte[] getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	@Override
	public String toString() {
		return "SSHFPResourceRecord [algorithm=" + algorithm
				+ ", fingerprintType=" + fingerprintType + "]";
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("algorithm", algorithm).
				add("fptype", fingerprintType).
				add("fingerprint", Hex.encodeHexString(fingerprint))).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeNumberField("algorithm", algorithm);
			g.writeNumberField("fptype", fingerprintType);
			g.writeObjectField("fingerprint",  Hex.encodeHexString(fingerprint));
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
