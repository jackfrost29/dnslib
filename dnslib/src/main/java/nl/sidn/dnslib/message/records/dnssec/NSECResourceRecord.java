package nl.sidn.dnslib.message.records.dnssec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import nl.sidn.dnslib.message.records.AbstractResourceRecord;
import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.TypeMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;

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
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		builder.
			add("rdata", Json.createObjectBuilder().
				add("next-domainname", nextDomainName));
		
		JsonArrayBuilder typeBuilder = Json.createArrayBuilder();
		for (TypeMap type : types) {
			typeBuilder.add(type.getType().name());
		}
		return builder.add("types", typeBuilder.build()).
			   build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeObjectField("next-domainname", nextDomainName);
			
			
			g.writeArrayFieldStart("types");
			for (TypeMap type : types) {
				g.writeString(type.getType().name());
			}
			g.writeEndArray();
			
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String getNextDomainName() {
		return nextDomainName;
	}





	public List<TypeMap> getTypes() {
		return types;
	}


	
	

}
