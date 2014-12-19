package dsn.bots.twitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import xml.XPathReader;
import dsn.bots.GeneralBot;
import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.scheduler.CalendarHelper;

public class TwitterBot extends GeneralBot {

	String userId = null;
	String urlFile = "twitter.txt";
	String urlBase = null;
	String urlFull = null;
	String screenName = null;
	String expression = null;
	String fileName = null;
	String urlDir = new java.io.File(".").getCanonicalPath() + "/urlData/";
	String xmlDir = new java.io.File(".").getCanonicalPath() + "/xmlData/twitter/";
	Integer counter;
	private String line;
	Integer ucount = 10;
	private String[] urlList = new String[ucount];
	private static XPathReader reader;  
	
	public TwitterBot() throws IOException {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	public String getSPName() {
		return "twitter";
	}
	
	public String getXmlFilename(String userId) {
		fileName = xmlDir + userId + ".xml";
		
		return fileName;
	}
	
	public void getUserInfo(String userId) {		
		System.out.println("getUserInfo for Twitter user: " + userId);
	}
	
	public void runAll(String userId, String token, String secret) throws IOException, ParserConfigurationException, SAXException, TransformerException, ParseException {
		String[][] messages = getMessages(token, secret);
		int size = messages[0].length;
		
		String[] user_id = new String[size];
		String[] sp_id = new String[size];
		
		for(int i = 0; i < size; i++) {
			user_id[i] = userId;
			sp_id[i] = "tw";
		}
		super.saveMessagesToDB(user_id, sp_id, messages[2], messages[1], messages[0]);
	}
	
	public String[][] getMessages(String token, String secret) throws ParseException {
		String resp = Utils.getUrl("http://api.twitter.com/1/statuses/user_timeline.json",
				ServiceProvider.TWITTER, token, secret);

		JSONParser jp = new JSONParser();
		JSONArray ja = (JSONArray)jp.parse(resp);
		JSONObject jo;
		
		String[][] messages = new String[3][ja.size()];
		
		for (int i = 0; i < ja.size(); i++)
		{
			jo = (JSONObject)ja.get(i);
			messages[0][i] = jo.get("id").toString();
			messages[1][i] = jo.get("created_at").toString();
			messages[2][i] = jo.get("text").toString();
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
		
		System.out.println("urlFull: " + urlFull);
		
		return urlFull;
	}
	
	public String getScreenName(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "/user/screen_name";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	public String getRealName(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "/user/name";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getLocation(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "/user/location";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getDescription(String urlFull) {
		reader = new XPathReader(urlFull);
		expression = "/user/description";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getUserId(String xmlPath) {
		reader = new XPathReader(xmlPath);
		String expression = "/user/id";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getStatus(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//text";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}

	public String getStatusId(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//status/id";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getStatusTime(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "//text/created_at";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getStatusCount(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "/user/statuses_count";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getFriendsCount(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "/user/friends_count";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
	
	public String getProfileImage(String xmlPath) {
		reader = new XPathReader(xmlPath);
		expression = "/user/profile_image_url";
		
		return reader.read(expression, XPathConstants.STRING).toString();
	}
}
