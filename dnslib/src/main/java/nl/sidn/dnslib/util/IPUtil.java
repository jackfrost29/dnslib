package nl.sidn.dnslib.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class IPUtil {

	public static String reverseIpv6(String ipv6){
		List<String> ipv6Parts = new ArrayList<>();
		String[] parts = StringUtils.split(ipv6, ":");
		int len = parts.length;
		for (int i = len-1; i >= 0; i--) {
			String part = parts[i];
			
			for (int j = 3; j >= 0; j--) {
				
				String octet = part.substring(j, j+1);
				ipv6Parts.add((String)octet);
			}
		}
		
		return StringUtils.join(ipv6Parts, ".") + ".ip6.arpa.";
	}
	
	public static String reverseIpv4(String ipv4){
		List<String> ipv4Parts = new ArrayList<>();
		String[] parts = StringUtils.split(ipv4, ".");
		int len = parts.length;
		for (int i = len-1; i >= 0; i--) {
			String part = parts[i];
			
			ipv4Parts.add(part);
		}
		
		return StringUtils.join(ipv4Parts, ".") + ".in-addr.arpa.";
	}
	
	public static String normalizeIpv6(String ipv6){
		String[] parts = StringUtils.splitByWholeSeparatorPreserveAllTokens("2001:db8::567:89ab", ":");
		List<String> normalizedParts = new ArrayList<>();
		
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if(part.length() == 0){
				//expand missing parts
				int missing = 8 - parts.length;
				for (int j = 0; j < missing; j++) {
					normalizedParts.add("0000");
				}
			}
			if(part.length() < 4){
				normalizedParts.add(StringUtils.leftPad(part, 4, "0"));
			}else{
				normalizedParts.add(part);
			}
		}
		
		
		return StringUtils.join(normalizedParts, ":");
	}
}
