package nl.sidn.dnslib.message;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;

import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.ResourceRecordClass;
import nl.sidn.dnslib.types.ResourceRecordType;

public class Question {
	
	private String qName;
	private ResourceRecordType qType;
	private ResourceRecordClass qClass;
	
	public Question(){};
	
	public Question(String qName, ResourceRecordType qType,	ResourceRecordClass qClass) {
		this.qName = qName;
		this.qType = qType;
		this.qClass = qClass;
	}
	public String getqName() {
		return qName;
	}
	public void setqName(String qName) {
		this.qName = qName;
	}
	public ResourceRecordType getqType() {
		return qType;
	}
	public void setqType(ResourceRecordType qType) {
		this.qType = qType;
	}
	public ResourceRecordClass getqClass() {
		return qClass;
	}
	public void setqClass(ResourceRecordClass qClass) {
		this.qClass = qClass;
	}

	public void decode(NetworkData buffer) {
	
		setqName(DNSStringUtil.readName(buffer));
		
		char type = buffer.readUnsignedChar();
		setqType(ResourceRecordType.fromValue(type));
		
		char qClass = buffer.readUnsignedChar();
		setqClass(ResourceRecordClass.fromValue(qClass));
		
	}


	@Override
	public String toString() {
		return "Question [qName=" + qName + ", qType=" + qType + ", qClass="
				+ qClass + "]";
	}
	
	public JsonObject toJSon(){
		return Json.createObjectBuilder().
			add("qName", qName).
			add("qType", qType != null?qType.name(): "").
			add("qClass", qClass != null?qClass.name(): "").
			build();
	}

	public void toJSon(JsonGenerator g) {
		
		try {
			g.writeStartObject();
			g.writeObjectField("qName", qName);
			g.writeObjectField("qType", qType != null?qType.name(): "");
			g.writeObjectField("qClass", qClass != null?qClass.name(): "");
			g.writeEndObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
