package nl.sidn.dnslib.message.records;

import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.ResourceRecordClass;
import nl.sidn.dnslib.types.ResourceRecordType;

public interface ResourceRecord{

	public String getName();

	public void setName(String name);

	public ResourceRecordType getType();

	public void setType(ResourceRecordType type);

	public ResourceRecordClass getClassz();

	public void setClassz(ResourceRecordClass classz);

	public long getTtl();

	public void setTtl(long ttl);
	
	public char getRdlength();
	
	public byte[] getRdata();
	
	void decode(NetworkData buffer);
	
	void encode(NetworkData buffer);

	public String toZone(int maxLength);
	

}
