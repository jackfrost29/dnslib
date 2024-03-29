package nl.sidn.dnslib.types;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * DNS RCODEs

	Registration Procedures
	IETF Review
	Reference
	[RFC-ietf-dnsext-rfc6195bis-05][RFC1035]
	
 * 	RCODE 	Name 	Description 	Reference 
		0	NoError	No Error	[RFC1035]
		1	FormErr	Format Error	[RFC1035]
		2	ServFail	Server Failure	[RFC1035]
		3	NXDomain	Non-Existent Domain	[RFC1035]
		4	NotImp	Not Implemented	[RFC1035]
		5	Refused	Query Refused	[RFC1035]
		6	YXDomain	Name Exists when it should not	[RFC2136]
		7	YXRRSet	RR Set Exists when it should not	[RFC2136]
		8	NXRRSet	RR Set that should exist does not	[RFC2136]
		9	NotAuth	Server Not Authoritative for zone	[RFC2136]
		9	NotAuth	Not Authorized	[RFC2845]
		10	NotZone	Name not contained in zone	[RFC2136]
		11-15	Unassigned		
		16	BADVERS	Bad OPT Version	[RFC-ietf-dnsext-rfc2671bis-edns0-10]
		16	BADSIG	TSIG Signature Failure	[RFC2845]
		17	BADKEY	Key not recognized	[RFC2845]
		18	BADTIME	Signature out of time window	[RFC2845]
		19	BADMODE	Bad TKEY Mode	[RFC2930]
		20	BADNAME	Duplicate key name	[RFC2930]
		21	BADALG	Algorithm not supported	[RFC2930]
		22	BADTRUNC	Bad Truncation	[RFC4635]
		23-3840	Unassigned		
		3841-4095	Reserved for Private Use		[RFC-ietf-dnsext-rfc6195bis-05]
		4096-65534	Unassigned		
		65535	Reserved, can be allocated by Standards Action		[RFC-ietf-dnsext-rfc6195bis-05]

 *
 */
public enum RcodeType {
	
	NO_ERROR((char)0),
	FORMAT_ERROR((char)1),
	SERVER_FAILURE((char)2),
	NXDOMAIN((char)3),
	NOT_IMPLEMENTED((char)4),
	REFUSED((char)5),
	RESERVED((char)65535);
	
	private char value;
	private static Map<Integer, RcodeType> rcodes = new HashMap<>();
	
	static{
		RcodeType[] values = values();
		for (RcodeType rcode : values) {
			rcodes.put(new Integer(rcode.getValue()), rcode);
		}
	}
	
	private RcodeType(char value){
		this.value = value;
	}

	public char getValue() {
		return value;
	}
	
	public static RcodeType fromValue(int value){
		return rcodes.get(new Integer(value));
	}
	

}
