package nl.sidn.dnslib.message.records;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.codehaus.jackson.JsonGenerator;


public class SPFResourceRecord extends TXTResourceRecord {

	private static final long serialVersionUID = 1L;
	
	/*
	 * https://tools.ietf.org/html/rfc4408#section-4.5
	 */
	
	public String getCacheId(){
		return null;
	}

	@Override
	public String toString() {
		return "SPFResourceRecord [value=" + value + "]";
	}
	

	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("spf-data", value)).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {

		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeObjectField("spf-data", value);
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




}
