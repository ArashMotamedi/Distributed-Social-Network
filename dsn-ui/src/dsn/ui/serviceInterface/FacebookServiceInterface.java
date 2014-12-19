package dsn.ui.serviceInterface;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;

public class FacebookServiceInterface extends ServiceInterface {

	@Override
	public BasicInfo getBasicInfo(String accessToken, String tokenSecret)
			throws Exception {
		BasicInfo info = new BasicInfo();
		// Gotta retrieve the name, the userid and the photourl

		String profile = Utils
				.getUrl("https://graph.facebook.com/me?access_token="
						+ accessToken);

		JSONObject jprofile = (JSONObject) (new JSONParser().parse(profile));

		info.setName((String) jprofile.get("name"));
		info.setUserid((String) jprofile.get("id"));
		info.setLink((String) jprofile.get("link"));

		// Ok, get the picture URL

		String url = Utils
				.getUrl("https://graph.facebook.com/me/picture?type=square&access_token="
						+ accessToken);
		info.setPhotoUrl(url);

		url = Utils
				.getUrl("https://graph.facebook.com/me/picture?type=large&access_token="
						+ accessToken);
		info.setLargePhotoUrl(url);

		return info;
	}

	@Override
	public String[] exchangeRequestWithAccess(String requestToken,
			String tokenSecret, String verifier) throws Exception {
		// Facebook doesn't really have all this dance!
		return new String[] { requestToken, tokenSecret };
	}

	@Override
	public Message[] getMessages(String accessToken, String tokenSecret)
			throws Exception {
		Message[] messages = null;

		String resp = Utils.getUrl("https://graph.facebook.com/me/statuses?access_token=" + accessToken);
		
		JSONParser jp = new JSONParser();
		JSONObject jo = (JSONObject) jp.parse(resp);
		JSONArray ja = (JSONArray) jo.get("data");

		int size = ja.size();
		messages = new Message[size];

		for (int i = 0; i < size; i++) {
			jo = (JSONObject) ja.get(i);

			messages[i] = new Message();
			messages[i].setServiceProvider(ServiceProvider.FACEBOOK);
			messages[i].setId(jo.get("id").toString());
			messages[i].setTimestamp(Utils.getCalendar(ServiceProvider.FACEBOOK, jo.get("updated_time").toString()));
			messages[i].setMessage(jo.get("message").toString());
		}

		return messages;
	}

}
