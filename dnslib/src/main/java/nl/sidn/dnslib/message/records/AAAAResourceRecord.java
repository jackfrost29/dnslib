package nl.sidn.dnslib.message.records;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import nl.sidn.dnslib.message.util.NetworkData;

import com.google.common.net.InetAddresses;

public class AAAAResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	/*
	 
	 A 128 bit IPv6 address is encoded in the data portion of an AAAA
   	resource record in network byte order (high-order byte first).
	
	 */
	
	private String address;
	private byte[] ipv6Bytes;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
		
		//get the 16 raw bytes for the ipv6 address
		ipv6Bytes = new byte[16];
		buffer.readBytes(ipv6Bytes);
		
		//create a textual representation of the address
		InetAddress ipv6Addres;
		try {
			ipv6Addres = InetAddress.getByAddress(ipv6Bytes);
		} catch (UnknownHostException e) {
			throw new RuntimeException("Illegal ipv6 address", e);
		}
		
		setAddress(InetAddresses.toAddrString(ipv6Addres));
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		//write rdlength
		buffer.writeChar(rdLength);
		
		buffer.writeBytes(ipv6Bytes);
	}

	@Override
	public String toString() {
		return super.toString() + " AAAAResourceRecord [address=" + address + "]";
	}
	
	public String getCacheId(){
		return address;
	}
	
	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + "\t" + address;
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("address", address)).
			build();
	}

	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeObjectField("address", address);
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

}
