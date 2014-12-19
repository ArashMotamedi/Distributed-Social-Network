package dsn.bots.facebook;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

//import json.*;
import dsn.bots.GeneralBot;
import dsn.commons.Utils;
import dsn.commons.development.Debug;
import dsn.scheduler.CalendarHelper;
 
/**
 * http://developers.facebook.com/docs/api
 * 
 * https://graph.facebook.com/ID/CONNECTION_TYPE
 * https://graph.facebook.com/me/feed
 * https://graph.facebook.com/me/friends
 * https://graph.facebook.com/<user_id>
 * http://informatics.systemsbiology.net/addama/code_samples
 * 
 * 10/17 - still testing out FB basic methods
 * 
 */


public class FacebookBot extends GeneralBot {
	
	String userId = null;
	String urlFile = "facebook.txt";
	String urlBase = null;
	String urlFull = null;
	String screenName = null;
	String expression = null;
	String fileName = null;
	String urlDir = new java.io.File(".").getCanonicalPath() + "/urlData/";
	String xmlDir = new java.io.File(".").getCanonicalPath() + "/xmlData/facebook/";
	Integer counter;
	Integer ucount = 10;
	String user_id = null;
	private String line;
	private String[] urlList = new String[ucount];
	
	public FacebookBot() throws IOException {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	public String getSPName() {
		return "facebook";
	}
	
	public String getXmlFilename(String userId) {
		fileName = xmlDir + userId + ".xml";

		return fileName;
	}
	
	public void getUserInfo(String userId) {		
		System.out.println("getUserInfo for Facebook user: " + userId);
	}
	
	public void runAll(String userId, String token, String secret) throws IOException, ParserConfigurationException, SAXException, TransformerException, ParseException {
		String[][] messages = getMessages(token);
		int size = messages[0].length;
		
		String[] user_id = new String[size];
		String[] sp_id = new String[size];
		
		for(int i = 0; i < size; i++) {
			user_id[i] = userId;
			sp_id[i] = "fb";
		}
		super.saveMessagesToDB(user_id, sp_id, messages[2], messages[1], messages[0]);
	}
	
	public String[][] getMessages(String token) throws ParseException {
		String resp = Utils
				.getUrl("https://graph.facebook.com/me/statuses?access_token="+token);
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(resp);
		JSONArray ja = (JSONArray) jo.get("data");

		int size = ja.size();		
		String[][] messages = new String[3][size];

		for (int i = 0; i < size; i++) {
			jo = (JSONObject) ja.get(i);
			messages[0][i] = jo.get("id").toString();
			messages[1][i] = CalendarHelper.formatTimestamp(jo.get("updated_time").toString());
			messages[2][i] = jo.get("message").toString();
		}

		return messages;		
	}

	public String getUrl(String userId, String token) {		

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
		
		return this.getUrlFull(urlList[0], userId, token);
	}
	
	public String getUrlFull(String urlBase, String userId, String token) {
		urlFull = urlBase + userId + "access_token=" +token;
		
		return urlFull;
	}
	/*
	public String getRealName(String xmlPath) throws IOException, JSONException {			
		String jsonString = super.readFileAsString(xmlPath);
 		JSONObject jsonObject = new JSONObject(jsonString);
		
		return jsonObject.getString("name");
	}
	
	public String getMessage(String xmlPath) throws IOException, JSONException {
		String jsonString = super.readFileAsString(xmlPath);
 		JSONObject jsonObject = new JSONObject(jsonString);
		
 		return jsonObject.getString("message");
	}
	
	public String getFirstName(String xmlPath) throws IOException, JSONException {				
		String jsonString = super.readFileAsString(xmlPath);
		JSONObject jsonObject = new JSONObject(jsonString);

		return jsonObject.getString("first_name");
	}
	
	public String getLastName(String xmlPath) throws IOException, JSONException {				
		String jsonString = super.readFileAsString(xmlPath);
		JSONObject jsonObject = new JSONObject(jsonString);

		return jsonObject.getString("last_name");
	}
	
	public String getLocale(String xmlPath) throws IOException, JSONException {				
		String jsonString = super.readFileAsString(xmlPath);
		JSONObject jsonObject = new JSONObject(jsonString);

		return jsonObject.getString("locale");
	}
	*/	
}
