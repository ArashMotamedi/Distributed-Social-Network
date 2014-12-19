import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;


public class GetTwitterMessages {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		
		String resp = Utils.getUrl("http://api.twitter.com/1/statuses/user_timeline.json",
				ServiceProvider.TWITTER, 
				"18650637-Znrvry77zXPZhbKW0qog8IZsVynZe9am7gpRLWhq9",
				"QE7qXkpeZiUocZaprzqRABV9mG3BH9jnIYCvCetiA");
		
		Debug.log(resp);


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
	}

}
