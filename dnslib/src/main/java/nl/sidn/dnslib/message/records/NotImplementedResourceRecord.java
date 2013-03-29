package nl.sidn.dnslib.message.records;

import nl.sidn.dnslib.message.util.NetworkData;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class NotImplementedResourceRecord extends AbstractResourceRecord {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NotImplementedResourceRecord.class);

	@Override
	public void decode(NetworkData buffer) {
		super.decode(buffer);
		
		LOGGER.debug("decode unknown RR with name: " + getName());
		
		LOGGER.debug(" Unknown RR has size: " + (int)rdLength);
		
		if(rdLength > 0 ){
			buffer.setReaderIndex(buffer.getReaderIndex()+rdLength);
		}
	
	}

	@Override
	public void encode(NetworkData buffer) {
		super.encode(buffer);
		
		buffer.writeChar(rdLength);
		buffer.writeBytes(rdata);
		
	}
	
	@Override
	public String toZone(int maxLength) {
		StringBuffer b = new StringBuffer();
		int paddedSize = ( maxLength - name.length() ) + name.length();
		String ownerWithPadding = StringUtils.rightPad(name, paddedSize, " ");
		
		b.append(ownerWithPadding + "\t" + ttl + "\t" );
		
		if(classz == null){
			b.append("CLASS" + (int)rawClassz) ;
		}else{
			b.append(classz);
		}
		
		b.append("\t");
		if(type == null){
			b.append("TYPE" + (int)rawType);
		}else{
			b.append(type);
		}
		b.append("\t");
		
		b.append("\\# " + (int)rdLength);
		
		if(rdLength > 0){
			b.append(" " + Hex.encodeHexString(rdata));
		}
		
		
		return b.toString();
		
	}

}
