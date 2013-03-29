package nl.sidn.dnslib.message.records.dnssec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.AlgorithmType;
import nl.sidn.dnslib.types.ResourceRecordType;
import nl.sidn.dnslib.types.TypeMap;
import nl.sidn.dnslib.util.LabelUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class RRSIGResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(RRSIGResourceRecord.class);
	
	
	/*
	   The RDATA for an RRSIG RR consists of a 2 octet Type Covered field, a
	   1 octet Algorithm field, a 1 octet Labels field, a 4 octet Original
	   TTL field, a 4 octet Signature Expiration field, a 4 octet Signature
	   Inception field, a 2 octet Key tag, the Signer's Name field, and the
	   Signature field.
	
	                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3
	    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |        Type Covered           |  Algorithm    |     Labels    |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |                         Original TTL                          |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |                      Signature Expiration                     |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |                      Signature Inception                      |
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   |            Key Tag            |                               /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+         Signer's Name         /
	   /                                                               /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   /                                                               /
	   /                            Signature                          /
	   /                                                               /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 
	 */

	
	private TypeMap typeCovered;
	private AlgorithmType algorithm;
	
	private short labels;
	private long originalTtl;
	private long signatureExpiration;
	private long signatureInception;
	private char keytag;
	private String signerName;
	private byte[] signature;
	private boolean wildcard;
		
	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
			
		char type = buffer.readUnsignedChar();

		ResourceRecordType rrType = ResourceRecordType.fromValue(type);
		if(rrType == null){
			rrType = ResourceRecordType.RESERVED;
		}
		
		typeCovered = new TypeMap(rrType, type);

		short alg = buffer.readUnsignedByte();
		algorithm = AlgorithmType.fromValue(alg);
		
		labels = buffer.readUnsignedByte();
		//check if wildacrd was used
		wildcard =  LabelUtil.count(getName()) > labels;
		
		originalTtl = buffer.readUnsignedInt();
		
		signatureExpiration = buffer.readUnsignedInt();
		
		signatureInception = buffer.readUnsignedInt();

		keytag = buffer.readUnsignedChar();
		
		signerName = DNSStringUtil.readName(buffer);
		
		int signatureLength = rdLength;
		if(signerName.length() == 1){
			//root
			signatureLength = signatureLength -1;
			
		}else{
			//non root signer
			signatureLength = signatureLength - (signerName.length() + 1);
		}
		signatureLength = signatureLength - 18;
		
		signature = new byte[signatureLength];
			
		buffer.readBytes(signature);
				
	}
	
	

	@Override
	public void encode(NetworkData buffer) {
		
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		
		buffer.writeChar(typeCovered.getValue());
		
		buffer.writeByte(algorithm.getValue());
		
		buffer.writeByte(labels);
		
		buffer.writeInt((int)originalTtl);
		
		buffer.writeInt((int)signatureExpiration);
		
		buffer.writeInt((int)signatureInception);
		
		buffer.writeChar(keytag);
		
		DNSStringUtil.writeName(signerName, buffer);
	
		buffer.writeBytes(signature);
		
	}


	public AlgorithmType getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}

	public short getLabels() {
		return labels;
	}

	public void setLabels(short labels) {
		this.labels = labels;
	}

	public long getOriginalTtl() {
		return originalTtl;
	}

	public void setOriginalTtl(long originalTtl) {
		this.originalTtl = originalTtl;
	}

	public long getSignatureExpiration() {
		return signatureExpiration;
	}

	public void setSignatureExpiration(long signatureExpiration) {
		this.signatureExpiration = signatureExpiration;
	}

	public long getSignatureInception() {
		return signatureInception;
	}

	public void setSignatureInception(long signatureInception) {
		this.signatureInception = signatureInception;
	}

	public char getKeytag() {
		return keytag;
	}

	public void setKeytag(char keytag) {
		this.keytag = keytag;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public char getRdLength() {
		return rdLength;
	}

	public void setRdLength(char rdLength) {
		this.rdLength = rdLength;
	}

	@Override
	public String toString() {
		return "RRSIGResourceRecord [typeCovered=" + typeCovered
				+ ", algorithm=" + algorithm + ", labels=" + labels
				+ ", originalTtl=" + originalTtl + ", signatureExpiration="
				+ signatureExpiration + ", signatureInception="
				+ signatureInception + ", keytag=" + (int)keytag + ", signerName="
				+ signerName + ", signature=" + Hex.encodeHexString(signature)
				+ ", rdLength=" + (int)rdLength + "]";
	}

	@Override
	public String toZone(int maxLength) {
		
		Date exp = new Date();
		exp.setTime((long)(signatureExpiration * 1000));
		
		Date incep = new Date();
		incep.setTime((long)(signatureInception * 1000));
		
		SimpleDateFormat fmt = new SimpleDateFormat("YYYYMMddHHmmss");
		fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		return super.toZone(maxLength) + "\t" + typeCovered.name() + " " + algorithm.getValue() + " " + labels +
				" " + originalTtl + " " + fmt.format(exp) +
				"(\n\t\t\t\t\t" + fmt.format(incep) + " " + (int)keytag + " " + signerName +
				 "\n\t\t\t\t\t" + new Base64(36, "\n\t\t\t\t\t".getBytes()).encodeAsString(signature) + " )" ;
	}

	public boolean getWildcard() {
		return wildcard;
	}

}
