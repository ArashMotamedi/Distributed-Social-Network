package dsn.ui.serviceInterface;

import dsn.commons.Utils;
import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;
import dsn.commons.development.Debug;
import dsn.commons.oauth.FlickrAuthUtils;

public class FlickrServiceInterface extends ServiceInterface {

	@Override
	public BasicInfo getBasicInfo(String accessToken, String tokenSecret) throws Exception {
		BasicInfo result = new BasicInfo();
		
		ConfigSet cs = new ConfigSet(ConfigType.OAUTH);
		
		FlickrAuthUtils fauth = new FlickrAuthUtils();
		String requestUrl = cs.getAttribute("flickrrequesturl");
		
		String query = "auth_token=" + accessToken + "&method=flickr.auth.checkToken";
		String resp = Utils.getUrl(requestUrl + "?" + fauth.getSignedQuery(query));
		
		String username, name;
		username = resp.substring(resp.indexOf("nsid=\"") + 6,
				resp.indexOf("\"", resp.indexOf("nsid=\"") + 6));
		
		name = resp.substring(resp.indexOf("fullname=\"") + 10,
				resp.indexOf("\"", resp.indexOf("fullname=\"") + 10));
				
		result.setName(name);
		result.setUserid(username);
		
		String photoUrl = "http://www.flickr.com/buddyicons/" + username + ".jpg";
		result.setPhotoUrl(photoUrl);
		result.setLargePhotoUrl(photoUrl);
		
		result.setLink("http://www.flickr.com/people/" + username);
		
		return result;
	}
	
	@Override
	public String[] exchangeRequestWithAccess(String requestToken, String tokenSecret, String verifier) throws Exception
	{
		// Facebook doesn't really have all this dance!
		return new String[] {new FlickrAuthUtils().getAccessToken(requestToken), ""};
	}

	@Override
	public Message[] getMessages(String accessToken, String tokenSecret) throws Exception
	{
		// No implementation for Flickr messages now
		return null;
	}
}
