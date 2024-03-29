package nl.sidn.dnslib.message.records;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.codehaus.jackson.JsonGenerator;

import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;

public class SOAResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private String mName;
	private String rName;
	private long serial;
	private long refresh;
	private long retry;
	private long expire;
	private long minimum;
	

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
	
		mName = DNSStringUtil.readName(buffer);
		
		rName = DNSStringUtil.readName(buffer);
		
		serial = buffer.readUnsignedInt();
		
		refresh = buffer.readUnsignedInt();
		
		retry = buffer.readUnsignedInt();
		
		expire = buffer.readUnsignedInt();
		
		minimum = buffer.readUnsignedInt();
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		/* length is names + leading size byte and terminating zero byte
		 * plus 5 4byte fields. 
		 */
	    char rdLength = (char) ((mName.length() + 2) + (rName.length() + 2) +  (5 * 4));
	    
	    //write rdlength
		buffer.writeChar(rdLength);
		
		DNSStringUtil.writeName(mName, buffer);
		
		DNSStringUtil.writeName(rName, buffer);
		
		buffer.writeInt( (int) serial);
		
		buffer.writeInt( (int) refresh);
		
		buffer.writeInt( (int) retry);
		
		buffer.writeInt( (int) expire);
		
		buffer.writeInt( (int) minimum);
		
	}
	
	public String getCacheId(){
		return null;
	}

	@Override
	public String toString() {
		return "SOAResourceRecord [mName=" + mName + ", rName=" + rName
				+ ", serial=" + serial + ", refresh=" + refresh + ", retry="
				+ retry + ", expire=" + expire + ", minimum=" + minimum + "]";
	}
	
	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + "\t" + mName + " " + rName + " " + serial + " " + refresh + " "
				+ retry + " " + expire + " " + minimum;
	}
	
	@Override
	public JsonObject toJSon(){
		JsonObjectBuilder builder = super.createJsonBuilder();
		return builder.
			add("rdata", Json.createObjectBuilder().
				add("mname", mName).
				add("rname", rName).
				add("serial", serial).
				add("refresh", refresh).
				add("retry", retry).
				add("expire", expire).
				add("minimum", minimum)).
			build();
	}
	
	@Override
	public void toJSon(JsonGenerator g) {
		try {
			super.toJSon(g);
			g.writeObjectFieldStart("rdata");
			g.writeObjectField("mname", mName);
			g.writeObjectField("rname", rName);
			g.writeObjectField("serial", serial);
			g.writeObjectField("refresh", refresh);
			g.writeObjectField("retry", retry);
			g.writeObjectField("expire", expire);
			g.writeObjectField("minimum", minimum);
			g.writeEndObject();
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getrName() {
		return rName;
	}

	public void setrName(String rName) {
		this.rName = rName;
	}

	public long getSerial() {
		return serial;
	}

	public void setSerial(long serial) {
		this.serial = serial;
	}

	public long getRefresh() {
		return refresh;
	}

	public void setRefresh(long refresh) {
		this.refresh = refresh;
	}

	public long getRetry() {
		return retry;
	}

	public void setRetry(long retry) {
		this.retry = retry;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public long getMinimum() {
		return minimum;
	}

	public void setMinimum(long minimum) {
		this.minimum = minimum;
	}

	public char getRdLength() {
		return rdLength;
	}

	public void setRdLength(char rdLength) {
		this.rdLength = rdLength;
	}
	
	

}
