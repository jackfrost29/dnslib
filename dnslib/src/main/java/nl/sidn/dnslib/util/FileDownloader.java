package nl.sidn.dnslib.util;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.log4j.Logger;

public class FileDownloader {
	
	private static final Logger LOGGER = Logger.getLogger(FileDownloader.class);

	
	public String download(String url){
		
		LOGGER.debug("download url: " + url);
		
		try {
			HttpClient httpclient = new DefaultHttpClient();
		
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "SIDN-DNS/1.0");
			 
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				StringBuilder builder = new StringBuilder();
				Scanner s = new Scanner(entity.getContent()).useDelimiter("\n");
				while (s.hasNext()) {
					builder.append(s.next() + "\n");
				}
			        
			     return builder.toString();
			}
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("Error while downloading url: " + url, e);
		}
		
		return null;
	}

}
