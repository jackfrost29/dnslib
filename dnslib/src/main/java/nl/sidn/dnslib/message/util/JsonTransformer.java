package nl.sidn.dnslib.message.util;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.sidn.dnslib.message.Header;
import nl.sidn.dnslib.message.Message;
import nl.sidn.dnslib.message.RRset;
import nl.sidn.dnslib.message.records.ResourceRecord;
import nl.sidn.dnslib.message.records.edns0.OPTResourceRecord;

import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;


public class JsonTransformer {
	
	
	public String transform(Message msg, long start,long duration){
		
		
		Map<String,Object> response = new LinkedHashMap<>();
		
		doTransform(response, msg, start, duration);
		
		Gson gs = new Gson();
		return gs.toJson(response);
	}
	


	private void doTransform(Map<String,Object> response,Message msg, long start,long duration ){
		List<RRset> rrsets = null;
		
		Map<String, Object> responseMap = new LinkedHashMap<>();
		response.put("response", responseMap);
		
		responseMap.put("start", new Date(start));
		responseMap.put("duration", duration + "ms");
		
		transform(responseMap, msg.getHeader());
			
		int count = (int)msg.getHeader().getAnCount();
		responseMap.put("anscount", count);
		
	
		if(count > 0){
			List<Map<String, Object>> list = new LinkedList<>();
			response.put("answers", list);
			rrsets = msg.getAnswer();	
			transform(list, rrsets, "answer");
		}
		
		count = (int)msg.getHeader().getNsCount();
		responseMap.put("nsscount", count);
		if(count > 0){
			List<Map<String, Object>> list = new LinkedList<>();
			response.put("authorities", list);
			rrsets = msg.getAnswer();	
			transform(list, rrsets, "authority");
		}


		count = (int)msg.getHeader().getArCount();
		responseMap.put("arcount", count);
		if(count > 0){
			List<Map<String, Object>> list = new LinkedList<>();
			response.put("additionals", list);
			rrsets = msg.getAnswer();	
			transform(list, rrsets, "additional");
		}


	}


	
	private void transform(List<Map<String, Object>> answers, List<RRset> rrsets, String groupname){
		
		for (RRset rrset : rrsets) {
			for (ResourceRecord rr : rrset.getAll()) {
				
				if(rr instanceof OPTResourceRecord){
					//skip the opt pseude rr
					continue;
				}
				Map<String, Object> answerMap = new LinkedHashMap<>();
				
				answerMap.put("name", rr.getName());
				
				answerMap.put("type", rr.getType().name());
				answerMap.put("class", rr.getClassz().name());
				answerMap.put("ttl", rr.getTtl());
				answerMap.put("rdlength", (int)rr.getRdlength());
				answerMap.put("rdata", Hex.encodeHexString(rr.getRdata()));
				
				Map<String, Object> groupMap = new LinkedHashMap<>();
				groupMap.put(groupname, answerMap);
				answers.add(groupMap);
			}
		}
		
	}
    
    private void transform(Map<String,Object> hdrMap, Header hdr){
		
		hdrMap.put("qr", (int)hdr.getQr().getValue());		
		hdrMap.put("opcode", (int)hdr.getOpCode().getValue());
		hdrMap.put("aa", hdr.isAa()?"1":"0");
		
		hdrMap.put("aa", hdr.isAa()?"1":"0");
		hdrMap.put("tc", hdr.isTc()?"1":"0");
		hdrMap.put("rd", hdr.isRd()?"1":"0");
		hdrMap.put("ra", hdr.isRa()?"1":"0");
		hdrMap.put("ad", hdr.isAd()?"1":"0");
		hdrMap.put("cd", hdr.isCd()?"1":"0");
		hdrMap.put("rcode", (int)hdr.getRcode().getValue());
	}

}
