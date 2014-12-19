package dsn.bots.flickr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;

import org.xml.sax.SAXException;

import dsn.bots.GeneralBot;
import xml.XPathReader;


// http://api.flickr.com/services/rest/?method=flickr.people.getInfo&api_key=1d9e0598c4987a51a011cdcebc7877b8&user_id=34225944@N00
//  need api_key and user_id for private data?
// http://api.flickr.com/services/rest/?method=flickr.people.getInfo&api_key=1d9e0598c4987a51a011cdcebc7877b8&user_id=34225944@N00
// api_key: 1d9e0598c4987a51a011cdcebc7877b8
// user_id: 1d9e0598c4987a51a011cdcebc7877b8 # dsn_cldulay0504

public class FlickrBot extends GeneralBot {

	String userId = null;
	String urlFile = "flickr.txt";
	String urlBase = null;
	String urlFull = null;
	String screenName = null;
	String expression = null;
	String fileName = null;
	String urlDir = new java.io.File(".").getCanonicalPath() + "/urlData/";
	String xmlDir = new java.io.File(".").getCanonicalPath() + "/xmlData/flickr/";
	Integer counter;
	private String line;
	Integer ucount = 10;
	private String[] urlList = new String[ucount];
	private static String apiKey = "1d9e0598c4987a51a011cdcebc7877b8";
	private static String apiSig = "8b8c49f9aa2c730d";
	String userIdFlickr = "54334448@N03"; // need to get this from the DB, but hard-coded for now for testing
	private static XPathReader reader;  
	
	public FlickrBot() throws IOException {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	public String getSPName() {
		return "flickr";
	}
	
	public String getXmlFilename(String userId) {
		fileName = xmlDir + userId + ".xml";
		
		return fileName;
	}

	public void getUserInfo(String userId) {
		//FlickrBot flickrBot = new FlickrBot();

		System.out.println("getUserInfo for Flickr user: " + userId);
	}
	
	public void runAll(String userId, String token) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		
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
		urlFull = urlBase + apiKey + "&user_id=" + userIdFlickr;
		System.out.println("urlFull: " + urlFull);
		
		return urlFull;
	}
	
	public String getScreenName(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//username";

		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getRealName(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "//person/realname";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getUserId(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//person/@id";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getPhotosCount(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//person/photos/count";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}

}
