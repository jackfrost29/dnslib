package nl.sidn.dnslib.message.records.dnssec;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.DigestType;

import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.JsonGenerator;

/**
 *  The RDATA of the NSEC3PARAM RR is as shown below:

                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |   Hash Alg.   |     Flags     |          Iterations           |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Salt Length  |                     Salt                      /
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

   Hash Algorithm is a single octet.

   Flags field is a single octet.

   Iterations is represented as a 16-bit unsigned integer, with the most
   significant bit first.

   Salt Length is represented as an unsigned octet.  Salt Length
   represents the length of the following Salt field in octets.  If the
   value is zero, the Salt field is omitted.

   Salt, if present, is encoded as a sequence of binary octets.  The
   length of this field is determined by the preceding Salt Length
   field.
 *
 */
public class NSEC3ParamResourceRecord extends AbstractResourceRecord{

	private static final long serialVersionUID = 1L;
	
	private DigestType hashAlgorithm;
	private short flags;
	//optout is the lsb of the flags octed
	private boolean optout;
	private char iterations;
	private short saltLength;
	private byte[] salt;
		
	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
		
		hashAlgorithm = DigestType.fromValue(buffer.readUnsignedByte());
		
		flags = buffer.readUnsignedByte();
		
		optout = (flags & 0x01) == 0x01; //0000 0001
		
		iterations = buffer.readUnsignedChar();
		
		saltLength = buffer.readUnsignedByte();
		
		if(saltLength > 0){
			salt = new byte[saltLength];
			buffer.readBytes(salt);
		}
		
		
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		
		buffer.writeBytes(rdata);
	}

	public DigestType getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(DigestType hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public short getFlags() {
		return flags;
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}

	public long getIterations() {
		return iterations;
	}

	public void setIterations(char iterations) {
		this.iterations = iterations;
	}

	public short getSaltLength() {
		return saltLength;
	}

	public void setSaltLength(short saltLength) {
		this.saltLength = saltLength;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	@Override
	public String toString() {
		return "NSEC3Param [ " +super.toString() + " hashAlgorithm=" + hashAlgorithm + ", flags="
				+ flags + ", optout=" + optout + ", iterations=" + (int)iterations + ", saltLength="
				+ saltLength + "]";
	}
	
	@Override
	public String toZone(int maxLength) {
		StringBuffer b = new StringBuffer();
		b.append(super.toZone(maxLength) + "\t" + hashAlgorithm.getValue() + " " + flags + " " +
				+ (int)iterations + " ");
		
		if(saltLength == 0){
			b.append("- ");
		}else{
			b.append(Hex.encodeHexString(salt) + " ");
		}
		
		return b.toString();
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("hash-algorithm", hashAlgorithm.name()).
				add("flags", flags).
				add("optout", optout).
				add("iterations", (int)iterations).
				add("salt-length", saltLength).
				add("salt", Hex.encodeHexString(salt))).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeObjectField("hash-algorithm",hashAlgorithm.name());
			g.writeNumberField("flags", flags);
			g.writeBooleanField("optout", optout);
			g.writeNumberField("iterations", (int)iterations);
			g.writeNumberField("salt-length", saltLength);
			g.writeObjectField("salt", Hex.encodeHexString(salt));
			
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
