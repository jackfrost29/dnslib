package nl.sidn.dnslib.message.records;

import java.io.IOException;
import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.ResourceRecordClass;
import nl.sidn.dnslib.types.ResourceRecordType;

public abstract class AbstractResourceRecord implements ResourceRecord, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected char rawType;
	protected char rawClassz;
	protected ResourceRecordType type;
	protected ResourceRecordClass classz;
	protected long ttl;
	protected char rdLength;
	protected byte[] rdata;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResourceRecordType getType() {
		return type;
	}
	public void setType(ResourceRecordType type) {
		this.type = type;
	}
	public ResourceRecordClass getClassz() {
		return classz;
	}
	public void setClassz(ResourceRecordClass classz) {
		this.classz = classz;
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	
	
	
	
	@Override
	public void decode(NetworkData buffer) {
		setName(DNSStringUtil.readName(buffer));
		
		rawType = buffer.readUnsignedChar();
		setType(ResourceRecordType.fromValue(rawType));
		rawClassz = buffer.readUnsignedChar();
		setClassz(ResourceRecordClass.fromValue(rawClassz));
		setTtl(buffer.readUnsignedInt());
		
		//read 16 bits rdlength field 
		rdLength = buffer.readUnsignedChar();
	
		rdata = readRdata(rdLength, buffer);
		
	}
	@Override
	public void encode(NetworkData buffer) {
		DNSStringUtil.writeName(getName(), buffer);
		
		buffer.writeChar(getType().getValue());
		
		buffer.writeChar(getClassz().getValue());
		
		buffer.writeInt((int)getTtl());
	}
	
	
	@Override
	public String toString() {
		return "name=" + name + ", type=" + type
				+ ", classz=" + classz + ", ttl=" + ttl + " rdlength=" + (int)rdLength;
	}

	protected byte[] readRdata(int rdlength, NetworkData buffer){
		buffer.markReaderIndex();
		byte[] rdata = new byte[rdlength];
		buffer.readBytes(rdata);
		buffer.resetReaderIndex();
		return rdata;
	}
	
	@Override
	public char getRdlength() {
		return rdLength;
	}
	
	@Override
	public byte[] getRdata() {
		return rdata;
	}
	
	@Override
	public String toZone(int maxLength) {
		
		int paddedSize = ( maxLength - name.length() ) + name.length();
		String ownerWithPadding = StringUtils.rightPad(name, paddedSize, " ");
		return ownerWithPadding + "\t" + ttl + "\t" + classz + "\t" + type;
	}
	
	public JsonObjectBuilder createJsonBuilder(){
		return Json.createObjectBuilder().
			add("name", name).
			add("type", type.name()).
			add("class", classz.name()).	
			add("ttl", ttl).
			add("rdLength", (int)rdLength);
	}
	
	public JsonObject toJSon(){
		JsonObjectBuilder builder = createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("dummy", "toddo")).
			build();
	}
	
	public void toJSon(JsonGenerator g){
		
		try {
			g.writeStartObject();
			g.writeObjectField("name", name);
			g.writeObjectField("type", type.name());
			g.writeObjectField("class", classz.name());	
			g.writeObjectField("ttl", ttl);
			g.writeObjectField("rdLength", (int)rdLength);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
