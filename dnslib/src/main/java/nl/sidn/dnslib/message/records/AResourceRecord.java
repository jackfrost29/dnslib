package nl.sidn.dnslib.message.records;

import nl.sidn.dnslib.message.util.NetworkData;

public class AResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private String address;
	private int[] ipv4Bytes;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
			
		if(rdLength != 4){
			//an address is 4bytes, so throw an error
			throw new RuntimeException("Invalid RDLENGTH");
		}
		
		ipv4Bytes = new int[4];
		
		StringBuffer addressBuffer = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			if(addressBuffer.length() > 0){
				addressBuffer.append(".");
			}
			int addressPart = buffer.readUnsignedByte();
			ipv4Bytes[i] = addressPart;
			addressBuffer.append(addressPart);
		}
		
		setAddress(addressBuffer.toString());
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		//write rdlength
		buffer.writeChar(rdLength);
		
		for (int i = 0; i < 4; i++) {
			buffer.writeByte( ipv4Bytes[i]);	
		}
	}
	
	public String getCacheId(){
		return address;
	}

	@Override
	public String toString() {
		return super.toString() + " AResourceRecord [address=" + address + "]";
	}
	
	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + "\t" + address;
	}
	

}
