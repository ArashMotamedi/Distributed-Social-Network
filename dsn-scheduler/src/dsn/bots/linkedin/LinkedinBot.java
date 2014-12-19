package dsn.bots.linkedin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import xml.XPathReader;
import dsn.bots.GeneralBot;
import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;

/**
 * linkedin requires oauth to access public and private data ??
 * @author crystaldulay
 *
 */
public class LinkedinBot extends GeneralBot {

	String userId = null;
	String urlFile = "linkedin.txt";
	String urlBase = null;
	String urlFull = null;
	String screenName = null;
	String expression = null;
	String fileName = null;
	String urlDir = new java.io.File(".").getCanonicalPath() + "/urlData/";
	String xmlDir = new java.io.File(".").getCanonicalPath() + "/xmlData/linkedin/";
	Integer counter;
	private String line;
	Integer ucount = 10;
	private String[] urlList = new String[ucount];
	private static XPathReader reader;  
	
	public LinkedinBot() throws IOException {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	public String getSPName() {
		return "linkedin";
	}
	
	public String getXmlFilename(String userId) {
		fileName = xmlDir + userId + ".xml";
		
		return fileName;
	}
	
	public void getUserInfo(String userId) {		
		System.out.println("getUserInfo for Linkedin user: " + userId);
	}
	
	public void runAll(String userId, String token, String secret) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		String[][] messages = getMessages(token, secret);
		int size = messages[0].length;
		
		String[] user_id = new String[size];
		String[] sp_id = new String[size];
		
		for(int i = 0; i < size; i++) {
			user_id[i] = userId;
			sp_id[i] = "li";
		}
		super.saveMessagesToDB(user_id, sp_id, messages[2], messages[1], messages[0]);	}
	
	public String[][] getMessages(String token, String secret)
	{
		String resp = Utils.getUrl("http://api.linkedin.com/v1/people/~/network/updates?scope=self&type=SHAR",
				ServiceProvider.LINKEDIN, token, secret);
		
		Document d = null;
		try
		{
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(resp)));
		} catch (Exception ex) {}
		
		NodeList nlTimestamp = d.getElementsByTagName("timestamp");
		NodeList nlUpdateKey = d.getElementsByTagName("update-key");
		NodeList nlStatus = d.getElementsByTagName("current-status");
		
		String[][] messages = new String[3][nlTimestamp.getLength()];
		
		for (int i = 0; i < nlTimestamp.getLength(); i++)
		{
			messages[0][i] = nlUpdateKey.item(i).getFirstChild().getNodeValue();
			messages[1][i] = nlTimestamp.item(i).getFirstChild().getNodeValue();
			messages[2][i] = nlStatus.item(i).getFirstChild().getNodeValue();
		}		
		return messages;
	}

	public String getUrl(String userId) {		

		File file;
	    BufferedReader reader;
	    counter = 0;
	    line = null;
	    
		try {
			file = new File(urlDir + urlFile);
			System.out.println("\nurl base file: " + file);
			reader = new BufferedReader(new FileReader(file));

			while((line = reader.readLine()) != null) {
				urlList[counter] = line;
				counter++;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return this.getUrlFull(urlList[0], userId);
	}
	
	public String getUrlFull(String urlBase, String userId) {
		urlFull = urlBase + userId;
		
		return urlFull;
	}

	public String getRealName(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "expression";
		
		return null;
	}
}
