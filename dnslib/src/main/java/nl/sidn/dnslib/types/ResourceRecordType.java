package nl.sidn.dnslib.types;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Resource Record (RR) TYPEs

	Reference
	[RFC-ietf-dnsext-rfc6195bis-05][RFC1035]
	Decimal 	Hexadecimal 	Registration Procedures 	Notes 
	0	0x0000	RRTYPE zero is used as a special indicator for the SIG RR [RFC2931], [RFC4034] and in other circumstances and must never be allocated for ordinary use.	
	1-127	0x0000-0x007F	DNS RRTYPE Allocation Policy	data TYPEs
	128-255	0x0080-0x00FF	DNS RRTYPE Allocation Policy	Q TYPEs, Meta TYPEs
	256-61439	0x0100-0xEFFF	DNS RRTYPE Allocation Policy	data RRTYPEs
	61440-65279	0xF000-0xFEFF	IETF Review	
	65280-65534	0xFF00-0xFFFE	Reserved for Private Use	
	65535	0xFFFF	Reserved (Standards Action)	
	TYPE 	Value 	Meaning 	Reference 	Registration Date 
	A	1	a host address	[RFC1035]	
	NS	2	an authoritative name server	[RFC1035]	
	MD	3	a mail destination (OBSOLETE - use MX)	[RFC1035]	
	MF	4	a mail forwarder (OBSOLETE - use MX)	[RFC1035]	
	CNAME	5	the canonical name for an alias	[RFC1035]	
	SOA	6	marks the start of a zone of authority	[RFC1035]	
	MB	7	a mailbox domain name (EXPERIMENTAL)	[RFC1035]	
	MG	8	a mail group member (EXPERIMENTAL)	[RFC1035]	
	MR	9	a mail rename domain name (EXPERIMENTAL)	[RFC1035]	
	NULL	10	a null RR (EXPERIMENTAL)	[RFC1035]	
	WKS	11	a well known service description	[RFC1035]	
	PTR	12	a domain name pointer	[RFC1035]	
	HINFO	13	host information	[RFC1035]	
	MINFO	14	mailbox or mail list information	[RFC1035]	
	MX	15	mail exchange	[RFC1035]	
	TXT	16	text strings	[RFC1035]	
	RP	17	for Responsible Person	[RFC1183]	
	AFSDB	18	for AFS Data Base location	[RFC1183][RFC5864]	
	X25	19	for X.25 PSDN address	[RFC1183]	
	ISDN	20	for ISDN address	[RFC1183]	
	RT	21	for Route Through	[RFC1183]	
	NSAP	22	for NSAP address, NSAP style A record	[RFC1706]	
	NSAP-PTR	23	for domain name pointer, NSAP style	[RFC1348][RFC1637][RFC1706]	
	SIG	24	for security signature	[RFC4034][RFC3755][RFC2535][RFC2536][RFC2537][RFC2931][RFC3110][RFC3008]	
	KEY	25	for security key	[RFC4034][RFC3755][RFC2535][RFC2536][RFC2537][RFC2539][RFC3008][RFC3110]	
	PX	26	X.400 mail mapping information	[RFC2163]	
	GPOS	27	Geographical Position	[RFC1712]	
	AAAA	28	IP6 Address	[RFC3596]	
	LOC	29	Location Information	[RFC1876]	
	NXT	30	Next Domain (OBSOLETE)	[RFC3755][RFC2535]	
	EID	31	Endpoint Identifier	[Michael_Patton][http://ana-3.lcs.mit.edu/~jnc/nimrod/dns.txt]	1995-06
	NIMLOC	32	Nimrod Locator	[1][Michael_Patton][http://ana-3.lcs.mit.edu/~jnc/nimrod/dns.txt]	1995-06
	SRV	33	Server Selection	[1][RFC2782]	
	ATMA	34	ATM Address	[ ATM Forum Technical Committee, "ATM Name System, V2.0", Doc ID: AF-DANS-0152.000, July 2000. Available from and held in escrow by IANA.]	
	NAPTR	35	Naming Authority Pointer	[RFC2915][RFC2168][RFC3403]	
	KX	36	Key Exchanger	[RFC2230]	
	CERT	37	CERT	[RFC4398]	
	A6	38	A6 (OBSOLETE - use AAAA)	[RFC3226][RFC2874][RFC6563]	
	DNAME	39	DNAME	[RFC6672]	
	SINK	40	SINK	[Donald_E_Eastlake][http://bgp.potaroo.net/ietf/all-ids/draft-eastlake-kitchen-sink-02.txt]	1997-11
	OPT	41	OPT	[RFC-ietf-dnsext-rfc2671bis-edns0-10][RFC3225]	
	APL	42	APL	[RFC3123]	
	DS	43	Delegation Signer	[RFC4034][RFC3658]	
	SSHFP	44	SSH Key Fingerprint	[RFC4255]	
	IPSECKEY	45	IPSECKEY	[RFC4025]	
	RRSIG	46	RRSIG	[RFC4034][RFC3755]	
	NSEC	47	NSEC	[RFC4034][RFC3755]	
	DNSKEY	48	DNSKEY	[RFC4034][RFC3755]	
	DHCID	49	DHCID	[RFC4701]	
	NSEC3	50	NSEC3	[RFC5155]	
	NSEC3PARAM	51	NSEC3PARAM	[RFC5155]	
	TLSA	52	TLSA	[RFC6698]	
	Unassigned	53-54			
	HIP	55	Host Identity Protocol	[RFC5205]	
	NINFO	56	NINFO	[Jim_Reid]	2008-01-21
	RKEY	57	RKEY	[Jim_Reid]	2008-01-21
	TALINK	58	Trust Anchor LINK	[Wouter_Wijngaards]	2010-02-17
	CDS	59	Child DS	[George_Barwood]	2011-06-06
	Unassigned	60-98			
	SPF	99		[RFC4408]	
	UINFO	100		[IANA-Reserved]	
	UID	101		[IANA-Reserved]	
	GID	102		[IANA-Reserved]	
	UNSPEC	103		[IANA-Reserved]	
	NID	104		[RFC6742]	
	L32	105		[RFC6742]	
	L64	106		[RFC6742]	
	LP	107		[RFC6742]	
	Unassigned	108-248			
	TKEY	249	Transaction Key	[RFC2930]	
	TSIG	250	Transaction Signature	[RFC2845]	
	IXFR	251	incremental transfer	[RFC1995]	
	AXFR	252	transfer of an entire zone	[RFC1035][RFC5936]	
	MAILB	253	mailbox-related RRs (MB, MG or MR)	[RFC1035]	
	MAILA	254	mail agent RRs (OBSOLETE - see MX)	[RFC1035]	
	*	255	A request for all records the server/cache has available	[RFC1035][RFC-ietf-dnsext-rfc6195bis-05]	
	URI	256	URI	[Patrik_Faltstrom]	2011-02-22
	CAA	257	Certification Authority Restriction	[RFC-ietf-pkix-caa-15]	2011-04-07
	Unassigned	258-32767			
	TA	32768	DNSSEC Trust Authorities	[Sam_Weiler][http://cameo.library.cmu.edu/][ Deploying DNSSEC Without a Signed Root. Technical Report 1999-19, Information Networking Institute, Carnegie Mellon University, April 2004.]	2005-12-13
	DLV	32769	DNSSEC Lookaside Validation	[RFC4431]	
	Unassigned	32770-65279			
	Private use	65280-65534			
	Reserved	65535			
 *
 */
public enum ResourceRecordType {
	
	A(1),
	AAAA(28),
	NS(2),
	CNAME(5),
	SOA(6),
	WKS(11),
	PTR(12),
	HINFO(13),
	MINFO(14),
	MX(15),
	TXT(16),
	LOC(29),
	SRV(33),
	NAPTR(35),
	OPT(41),
	SSHFP(44),
	DNSKEY(48),
	RRSIG(46),
	NSEC(47),
	DS(43),
	NSEC3(50),
	NSEC3PARAM(51),
	SPF(99),
	AXFR(252),
	MAILB(253),
	MAILA(254),
	ANY(255),
	RESERVED(65535);	
	
	private int value;
	
	private static Map<String, ResourceRecordType> types = new HashMap<>();
	private static Map<Integer, ResourceRecordType> typesToInt = new HashMap<>();
	
	static{
		ResourceRecordType[] values = values();
		for (ResourceRecordType type : values) {
			types.put(type.name(), type);
			typesToInt.put(new Integer(type.getValue()), type);
		}
	}
	
	private ResourceRecordType(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
	public static ResourceRecordType fromString(String name){
		return types.get(StringUtils.upperCase(name));
	}
	
	public static ResourceRecordType fromValue(int value){
		return typesToInt.get(new Integer(value));
	}
}
