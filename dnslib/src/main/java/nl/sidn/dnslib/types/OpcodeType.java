package nl.sidn.dnslib.types;

/**
 * 
 * DNS OpCodes

	Registration Procedures
	Standards Action as modified by [RFC4020]
	Reference
	[RFC-ietf-dnsext-rfc6195bis-05][RFC1035]
	OpCode 	Name 	Reference 
	0	Query	[RFC1035]
	1	IQuery (Inverse Query, OBSOLETE)	[RFC3425]
	2	Status	[RFC1035]
	3	Unassigned	
	4	Notify	[RFC1996]
	5	Update	[RFC2136]
	6-15	Unassigned	

 *
 */
public enum OpcodeType {
	
	STANDARD(0),
	INVERSE(1),
	STATUS(2);
	
	private int value;
	
	private OpcodeType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	
}
