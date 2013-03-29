package nl.sidn.dnslib.message.records;

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
	

}
