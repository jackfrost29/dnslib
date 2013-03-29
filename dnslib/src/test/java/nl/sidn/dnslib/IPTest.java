package nl.sidn.dnslib;

import nl.sidn.dnslib.util.IPUtil;

import org.junit.Test;


public class IPTest {

	@Test
	public void expand(){
		
		String ipv6 =  IPUtil.normalizeIpv6("2001:db8::567:89ab");
		System.out.println("ipv6: " + ipv6);
		
		String ipv6Octets = IPUtil.reverseIpv6(ipv6);
		System.out.println("ipv6Octets: " + ipv6Octets);
		
		String ipv4reversed = IPUtil.reverseIpv4("192.168.0.1");
		System.out.println("ipv4reversed: " + ipv4reversed);
	}
	
	
}
