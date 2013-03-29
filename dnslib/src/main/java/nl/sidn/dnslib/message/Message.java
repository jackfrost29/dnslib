package nl.sidn.dnslib.message;

import java.util.ArrayList;
import java.util.List;

import nl.sidn.dnslib.message.records.ResourceRecord;
import nl.sidn.dnslib.message.records.ResourceRecordFactory;
import nl.sidn.dnslib.message.records.edns0.OPTResourceRecord;
import nl.sidn.dnslib.message.util.DNSStringUtil;
import nl.sidn.dnslib.message.util.NetworkData;
import nl.sidn.dnslib.types.ResourceRecordType;

import org.apache.log4j.Logger;


public class Message {
	
	private static final Logger LOGGER = Logger.getLogger(Message.class);
	
	private Header header;
	
	private List<Question> questions = new ArrayList<>();
	private List<RRset> answer = new ArrayList<>();
	private List<RRset> authority = new ArrayList<>();
	private List<RRset> additional = new ArrayList<>();
	
	public Message(){};
	
	public Header getHeader() {
		return header;
	}

	public Message addHeader(Header header) {
		this.header = header;
		updateHeaderCounters();
		return this;
	}
	
	public Message build(){
		updateHeaderCounters();
		return this;
	}
	
	public void updateHeaderCounters() {
		this.header.setQdCount((char)questions.size());
		this.header.setAnCount((char)rrsetSize(answer));
		this.header.setNsCount((char)rrsetSize(authority));
		this.header.setArCount((char)rrsetSize(additional));
	}
	
	private int rrsetSize(List<RRset> rrsets){
		int count = 0;
		for (RRset rrset : rrsets) {
			count = count + rrset.size();
		}
		return count;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public Message addQuestion(Question question) {
		this.questions.add(question);
		return this;
	}
	
	public List<RRset> getAnswer() {
		return answer;
	}

	private RRset findRRset(List<RRset> setList, ResourceRecord rr){
		for (RRset rrset : setList) {
			if(rrset.getOwner().equalsIgnoreCase(rr.getName()) &&
					rrset.getClassz() == rr.getClassz() &&
					rrset.getType() == rr.getType() ){
				return rrset;
			}
		}
		
		return null;
	}
	
	private RRset createRRset(List<RRset> setList, ResourceRecord rr){
		RRset rrset = RRset.createAs(rr);
		setList.add(rrset);
		return rrset;
	}
	
	public void addAnswer(ResourceRecord answer) {
		RRset rrset = findRRset(this.answer, answer);
		if(rrset == null){
			rrset = createRRset(this.answer, answer);
		}else{
			rrset.add(answer);
		}
	}
	
	public void addAnswer(RRset rrset) {
		answer.add(rrset);
	}

	public List<RRset> getAuthority() {
		return authority;
	}

	public void addAuthority(ResourceRecord authority) {
		RRset rrset = findRRset(this.authority, authority);
		if(rrset == null){
			rrset = createRRset(this.authority, authority);
		}else{
			rrset.add(authority);
		}
	}
	
	public void addAuthority(RRset authority) {
		this.authority.add(authority);

	}

	public List<RRset> getAdditional() {
		return additional;
	}

	public void addAdditional(ResourceRecord rr) {
		RRset rrset = findRRset(this.additional, rr);
		if(rrset == null){
			rrset = createRRset(this.additional, rr);
		}else{
			rrset.add(rr);
		}
	}
	
	public void addAdditional(RRset additional) {
		if(additional.getType() != ResourceRecordType.OPT){
			this.additional.add(additional);
		}
	}
	

	public void decode(NetworkData buffer) {
		//LOGGER.debug("Message size: " + buffer.readableBytes());
		
		header = new Header();
		header.decode(buffer);
		
		//LOGGER.debug("decoded header:" + header);
		
		//decode all questions in the message
		for(int i = 0; i < header.getQdCount(); i++){
			Question question = decodeQuestion(buffer);
			//LOGGER.debug("decoded question:" + question);
			addQuestion(question);
		}
		
		for(int i = 0; i < header.getAnCount(); i++){
			ResourceRecord rr = decodeResourceRecord(buffer);
			//LOGGER.debug("decoded rr:" + rr);
			addAnswer(rr);
		}
		
		for(int i = 0; i < header.getNsCount(); i++){
			ResourceRecord rr = decodeResourceRecord(buffer);
			//LOGGER.debug("decoded rr:" + rr);
			addAuthority(rr);
		}
		
		for(int i = 0; i < header.getArCount(); i++){
			ResourceRecord rr = decodeResourceRecord(buffer);
			//LOGGER.debug("decoded rr:" + rr);
			addAdditional(rr);
		}
		
		/* not all RR may have been decoded into the message
		 * to make sure the section counters are correct do an update
		 * of the counters now. 
		 */
		updateHeaderCounters();
	}
	
	private ResourceRecord decodeResourceRecord(NetworkData buffer) {
		
		/* read ahead to the type bytes to find out what
		 * type of RR needs to be created.
		 */

		//skip 16bits with name
		buffer.markReaderIndex();

		//read the name so we kan get to the type bytes after the name
		DNSStringUtil.readName(buffer);
		
		
		//read 16 bits with type
		
		int type = buffer.readUnsignedChar();
		
		//LOGGER.debug("decoding rr with name:" + name + " and with type:" + type);
		
		//go back bits to the start of the RR
		buffer.resetReaderIndex();
		
		ResourceRecord rr = ResourceRecordFactory.getInstance().createResourceRecord(type);
	
		if(rr != null){
			//decode the entire rr now
			rr.decode(buffer);
		}
		
		return rr;
	}


	private Question decodeQuestion(NetworkData buffer) {
		
		Question question = new Question();
		
		question.decode(buffer);
			
		return question;		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nheader\n");
		builder.append("_______________________________________________\n");
		builder.append( "Message [header=" + header + "] ");
		builder.append("\n");
		
		builder.append("question\n");
		builder.append("_______________________________________________\n");
		for (Question question : questions) {
			builder.append(question.toString());
			builder.append("\n");
		}
		
		builder.append("answer\n");
		builder.append("_______________________________________________\n");
		for (RRset rrset : answer) {
			builder.append(rrset.toString());
			builder.append("\n");
		}
		
		builder.append("authority\n");
		builder.append("_______________________________________________\n");
		for (RRset rrset : authority) {
			builder.append(rrset.toString());
			builder.append("\n");
		}
		
		builder.append("additional\n");
		builder.append("_______________________________________________\n");
		for (RRset rrset : additional) {
			builder.append(rrset.toString());
			builder.append("\n");
		}

		return builder.toString();
	}

	public Object toZone() {
		StringBuilder builder = new StringBuilder();
		builder.append("; header: " + header.toZone() + "\n");

		int maxLength = maxLength();
		System.out.println("maxlength = " + maxLength);
		builder.append("; answer section:\n");
		for (RRset rrset : answer) {
			builder.append(rrset.toZone(maxLength));
			//builder.append("\n");
		}
		
		builder.append("; authority section:\n");
		for (RRset rrset : authority) {
			builder.append(rrset.toZone(maxLength));
			//builder.append("\n");
		}
		
		builder.append("; additional section:\n");
		for (RRset rrset : additional) {
			builder.append(rrset.toZone(maxLength));
			//builder.append("\n");
		}

		return builder.toString();
	}
	
	public int maxLength() {
		int length = 0;
		
		for (RRset rrset : answer) {
			if(rrset.getOwner().length() > length){
				length = rrset.getOwner().length();
			}
		}
		
		for (RRset rrset : authority) {
			if(rrset.getOwner().length() > length){
				length = rrset.getOwner().length();
			}
		}
		
		for (RRset rrset : additional) {
			if(rrset.getOwner().length() > length){
				length = rrset.getOwner().length();
			}
		}

		return length;
	}

	
}
