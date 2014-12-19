package dsn.ui.serviceInterface;

import dsn.commons.Utils.ServiceProvider;

public abstract class ServiceInterface {
	public abstract BasicInfo getBasicInfo(String accessToken, String tokenSecret) throws Exception;
	public abstract String[] exchangeRequestWithAccess(String requestToken, String tokenSecret, String verifier) throws Exception;
	public abstract Message[] getMessages(String accessToken, String tokenSecret) throws Exception;
	
	public final static ServiceInterface getInterface(ServiceProvider serviceProvider)
	{
		ServiceInterface result = null;
		
		switch (serviceProvider) {
		case FACEBOOK:	
			result = new FacebookServiceInterface();
			break;
		case TWITTER:
			result = new TwitterServiceInterface();
			break;
		case LINKEDIN:
			result = new LinkedinServiceInterface();
			break;
		case FLICKR:
			result = new FlickrServiceInterface();
			break;
		}
		
		return result;
	}
}
