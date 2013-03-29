package nl.sidn.dnslib.message.records;


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
	





}
