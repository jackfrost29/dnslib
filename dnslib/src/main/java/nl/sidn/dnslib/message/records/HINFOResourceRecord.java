package nl.sidn.dnslib.message.records;

import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;

public class HINFOResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private String cpu;
	private String os;		

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
		
		cpu = DNSStringUtil.readName(buffer);
		
		os = DNSStringUtil.readName(buffer);
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(cpu.length() + os.length() + 4);
		DNSStringUtil.writeName(cpu, buffer);
		DNSStringUtil.writeName(os, buffer);
		
	}
	
	public String getCacheId(){
		return null;
	}

	@Override
	public String toString() {
		return "HINFOResourceRecord [cpu=" + cpu + ", os=" + os + "]";
	}

	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + "\t" + cpu + " " + os;
	}
	

}
