package dsn.ui.serviceInterface;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;
import dsn.commons.oauth.OAuthUtils;

public class TwitterServiceInterface extends ServiceInterface {

	@Override
	public BasicInfo getBasicInfo(String accessToken, String tokenSecret) {
		BasicInfo result = new BasicInfo();
		
		String resp = Utils.getUrl("http://api.twitter.com/1/account/verify_credentials.xml", ServiceProvider.TWITTER, accessToken, tokenSecret);
		
		result.setName(Utils.getXmlValue(resp, "name"));
		result.setUserid(Utils.getXmlValue(resp, "id"));
		result.setLink("http://twitter.com/" + Utils.getXmlValue(resp, "screen_name"));
		
		resp = Utils.getUrl("http://api.twitter.com/1/users/profile_image/" + result.getUserid() + ".json?size=bigger", ServiceProvider.TWITTER, accessToken, tokenSecret);
		result.setPhotoUrl(resp);
		result.setLargePhotoUrl(resp);
		
		
		return result;
	}

	@Override 
	public String[] exchangeRequestWithAccess(String requestToken, String tokenSecret, String verifier) throws Exception
	{
		OAuthUtils oa = new OAuthUtils();
		return oa.getAccessTokens(ServiceProvider.TWITTER, requestToken, tokenSecret, verifier);
	}

	@Override
	public Message[] getMessages(String accessToken, String tokenSecret) throws Exception
	{
		Message[] messages = null;

		String resp = Utils.getUrl("http://api.twitter.com/1/statuses/user_timeline.json",
				ServiceProvider.TWITTER, accessToken, tokenSecret);

		JSONParser jp = new JSONParser();
		JSONArray ja = (JSONArray)jp.parse(resp);
		JSONObject jo;
		
		messages = new Message[ja.size()];
		
		for (int i = 0; i < messages.length; i++)
		{
			jo = (JSONObject)ja.get(i);
			messages[i] = new Message();
			messages[i].setServiceProvider(ServiceProvider.TWITTER);
			messages[i].setId(jo.get("id").toString());
			messages[i].setTimestamp(Utils.getCalendar(ServiceProvider.TWITTER, jo.get("created_at").toString()));
			messages[i].setMessage(jo.get("text").toString());
		}		
		return messages;
	}
}
