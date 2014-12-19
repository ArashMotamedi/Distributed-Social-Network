import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dsn.commons.Utils;
import dsn.commons.development.Debug;


public class GetFacebookMessages {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		
		String resp = Utils.getUrl("https://graph.facebook.com/me/links?access_token=160295784005665|e418328f71139a8478896ad6-516938108|freFMIKkPAOrEohX0r5k7wZXR-A");
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject)jp.parse(resp);
		JSONArray ja = (JSONArray)jo.get("data");
		
		int size = ja.size();
		String[][] messages = new String[size][3];
		
		for (int i = 0; i < size; i++)
		{
			jo = (JSONObject)ja.get(i);
			messages[i][0] = jo.get("id").toString();
			messages[i][1] = jo.get("created_time").toString();
			messages[i][2] = jo.get("message").toString();
		}
		
		Debug.log("");
	}

}
