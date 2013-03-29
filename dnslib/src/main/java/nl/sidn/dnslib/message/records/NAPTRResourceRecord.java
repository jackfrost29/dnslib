package nl.sidn.dnslib.message.records;

import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;

public class NAPTRResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	
	private char order;
	private char preference;
	private String flags;
	private String services;
	private String regexp;
	private String replacement;
	
	private int length;

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
	
		order = buffer.readUnsignedChar();
		
		preference = buffer.readUnsignedChar();

		flags = DNSStringUtil.readCharacterString(buffer);
		length = 5 + flags.length();
		
		services = DNSStringUtil.readCharacterString(buffer);
		length = length + services.length() + 1; //3x16 bits + 1byte services length
		
		regexp = DNSStringUtil.readCharacterString(buffer);
		length = length + regexp.length() + 1;
		
		replacement = DNSStringUtil.readName(buffer);
		if(replacement == null || replacement.length() == 0){
			length = length + 1; //zero byte only	
		}else{
			length = length + replacement.length() + 1;
		}
		
	}


	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(length);
		
		buffer.writeChar(preference);
		
		DNSStringUtil.writeCharacterString(flags, buffer);
		
		DNSStringUtil.writeCharacterString(services, buffer);
		
		DNSStringUtil.writeCharacterString(regexp, buffer);
		
		DNSStringUtil.writeName(replacement, buffer);
	}
	
	public String getCacheId(){
		return null;
	}

	@Override
	public String toString() {
		return "NAPTRResourceRecord [" + super.toString() + ", order=" + (int)order + ", preference="
				+ (int)preference + ", flags=" + flags + ", services=" + services
				+ ", regexp=" + regexp + ", replacement=" + replacement
				+ ", length=" + length + "]";
	}

	@Override
	public String toZone(int maxLength) {
		return super.toZone(maxLength) + "\t" + (int)order + " " + preference +
				" \"" + flags + "\" " + "\"" + services + "\" " +
				"\"" + regexp + "\" "+ replacement;
	}





}
