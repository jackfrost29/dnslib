package nl.sidn.dnslib.message.records.dnssec;

import java.util.ArrayList;
import java.util.List;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.TypeMap;

import org.apache.log4j.Logger;

public class NSECResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	/*
	     The RDATA of the NSEC RR is as shown below:

	                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3
	    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   /                      Next Domain Name                         /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	   /                       Type Bit Maps                           /
	   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 
	 */
	private static final Logger LOGGER = Logger.getLogger(NSECResourceRecord.class);
	

	private String nextDomainName;
	protected List<TypeMap> types = new ArrayList<>();
	
	
	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);

		nextDomainName = DNSStringUtil.readName(buffer);
		int octetAvailable = rdLength - (nextDomainName.length() + 1);
		new NSECTypeDecoder().decode(octetAvailable, buffer, types);
	}
	
	
	


	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		
		//DNSStringUtil.writeName(nextDomainName, buffer);
			
		buffer.writeBytes(getRdata());
	}

	@Override
	public String toString() {
		return "NSECResourceRecord [rdLength=" + (int)rdLength + ", nextDomainName="
				+ nextDomainName + "]";
	}


	@Override
	public String toZone(int maxLength) {
		StringBuffer b = new StringBuffer();
		b.append(super.toZone(maxLength) + "\t" + nextDomainName + " ");
		
		for (TypeMap type : types) {
			b.append(type.name() + " ");
		}
		
		return b.toString();
				
	}



	public String getNextDomainName() {
		return nextDomainName;
	}





	public List<TypeMap> getTypes() {
		return types;
	}


	
	

}
