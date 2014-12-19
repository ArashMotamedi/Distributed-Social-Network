package dsn.ui;

import java.util.ArrayList;

import dsn.commons.Utils;
import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.InteropHandler;
import dsn.commons.oauth.FlickrAuthUtils;
import dsn.commons.oauth.OAuthUtils;
import dsn.commons.Utils.ServiceProvider;
import dsn.ui.serviceInterface.BasicInfo;
import dsn.ui.serviceInterface.FacebookServiceInterface;
import dsn.ui.serviceInterface.FlickrServiceInterface;
import dsn.ui.serviceInterface.LinkedinServiceInterface;
import dsn.ui.serviceInterface.Message;
import dsn.ui.serviceInterface.ServiceInterface;
import dsn.ui.serviceInterface.TwitterServiceInterface;

public class UIServiceHandler extends InteropHandler {
	public InteropTransferObject getAuthenticationUrl(
			InteropTransferObject params) throws Exception {
		InteropTransferObject to = new InteropTransferObject();

		// Which service is this?
		String serviceProvider = params.getValue("serviceProvider")
				.toLowerCase();

		// Determine the service provider
		ServiceProvider sp = Utils.getServiceProvider(serviceProvider);

		String url = "";
		if (sp == ServiceProvider.FLICKR) {
			FlickrAuthUtils fauth = new FlickrAuthUtils();
			url = fauth.getAuthenticationUrl();
		} else {
			OAuthUtils oauth = new OAuthUtils();
			String[] tokens = oauth.getAuthenticationUrl(sp);
			url = tokens[2];
			to.add("token", tokens[0]);
			to.add("secret", tokens[1]);
		}

		to.add("url", url);

		return to;
	}

	public InteropTransferObject updateAuthorizations(
			InteropTransferObject params) throws Exception {
		// What is included in the authorization log?
		String[] authRecords = params.getValues("authRecords");
		InteropTransferObject result = null;

		if (authRecords.length > 0) {
			ArrayList<String> userid = new ArrayList<String>();
			ArrayList<String> service = new ArrayList<String>();
			ArrayList<String> accessToken = new ArrayList<String>();
			ArrayList<String> tokenSecret = new ArrayList<String>();
			ArrayList<String> spUsername = new ArrayList<String>();
			ArrayList<String> spName = new ArrayList<String>();
			ArrayList<String> spImageUrl = new ArrayList<String>();
			ArrayList<String> spLargeImageUrl = new ArrayList<String>();
			ArrayList<String> spLink = new ArrayList<String>();

			result = new InteropTransferObject();

			// Ok, process each authorization record
			for (int i = 0; i < authRecords.length; i++) {
				try
				{
					// Parse the line first!
					String[] pieces = authRecords[i].split(":");
					// UserID:token:secret:query
					
					String in_userid = pieces[0],
							in_token = pieces[1],
							in_secret = pieces[2],
							in_query = pieces[3];
					
					String verifier = "";
	
					// Record the service provider
					ServiceProvider sp = Utils.getServiceProvider(Utils.getQueryValue(in_query, "src"));				
					ServiceInterface si = ServiceInterface.getInterface(sp);
					
					// Some service specific pre-processing
					if (sp == ServiceProvider.FACEBOOK)
					{
						in_token = Utils.getQueryValue(in_query, "access_token");
						verifier = "";
					}
					else if (sp == ServiceProvider.LINKEDIN || sp == ServiceProvider.TWITTER)
					{
						verifier = Utils.getQueryValue(in_query, "oauth_verifier");
					}
					else if (sp == ServiceProvider.FLICKR)
					{
						in_token = Utils.getQueryValue(in_query, "frob");
						verifier = "";
					}
					
					String[] tokens = si.exchangeRequestWithAccess(in_token, in_secret, verifier);
					BasicInfo bi = si.getBasicInfo(tokens[0], tokens[1]);
					
					// Record the values
					userid.add(in_userid);
					service.add(sp.name());
					accessToken.add(tokens[0]);
					tokenSecret.add(tokens[1]);
					spUsername.add(bi.getUserid());
					spName.add(bi.getName());
					spImageUrl.add(bi.getPhotoUrl());
					spLargeImageUrl.add(bi.getLargePhotoUrl());
					spLink.add(bi.getLink());
				}
				catch (Exception ex) { throw ex; }
			}

			// Ok, put everything in the result object
			int arraySize = userid.size();
			
			result.add("userid", userid.toArray(new String[arraySize]));
			result.add("service", service.toArray(new String[arraySize]));
			result.add("token",	accessToken.toArray(new String[arraySize]));
			result.add("secret", tokenSecret.toArray(new String[arraySize]));
			result.add("spUsername", spUsername.toArray(new String[arraySize]));
			result.add("spName", spName.toArray(new String[arraySize]));
			result.add("spImageUrl", spImageUrl.toArray(new String[arraySize]));
			result.add("spLargeImageUrl", spLargeImageUrl.toArray(new String[arraySize]));
			result.add("spLink", spLink.toArray(new String[arraySize]));
		}

		return result;
	}
	
	private BasicInfo getBasicInfo(ServiceProvider serviceProvider, String token, String secret) throws Exception
	{
		BasicInfo result = null;
		ServiceInterface si = ServiceInterface.getInterface(serviceProvider);		
		
		// Get the basic info
		result = si.getBasicInfo(token, secret);
		
		return result; 
	}

	public InteropTransferObject getUserBasicInfo(InteropTransferObject params) throws Exception
	{
		InteropTransferObject result = new InteropTransferObject();
		
		// Read the parameters
		if (params.containsKey("facebookAccessToken")) {
			// Kick off facebook!
			FacebookServiceInterface fbi = new FacebookServiceInterface();
			BasicInfo fbinfo = fbi.getBasicInfo(params.getValue("facebookAccessToken"), null);
			result.add("facebookUserid", fbinfo.getUserid());
			result.add("facebookName", fbinfo.getName());
			result.add("facebookPhotoUrl", fbinfo.getPhotoUrl());
		}
		
		if (params.containsKey("twitterAccessToken")) {
			// Check out twitter!
			TwitterServiceInterface twi = new TwitterServiceInterface();
			BasicInfo twinfo = twi.getBasicInfo(params.getValue("twitterAccessToken"), params.getValue("twitterTokenSecret"));
			result.add("twitterUserid", twinfo.getUserid());
			result.add("twitterName", twinfo.getName());
			result.add("twitterPhotoUrl", twinfo.getPhotoUrl());
		}

		if (params.containsKey("linkedinAccessToken")) {
			// Check out linkedin!
			LinkedinServiceInterface lii = new LinkedinServiceInterface();
			BasicInfo liinfo = lii.getBasicInfo(params.getValue("linkedinAccessToken"), params.getValue("linkedinTokenSecret"));
			result.add("linkedinUserid", liinfo.getUserid());
			result.add("linkedinName", liinfo.getName());
			result.add("linkedinPhotoUrl", liinfo.getPhotoUrl());
		}
		
		if (params.containsKey("flickrAccessToken")) {
			// Check out flickr!
			FlickrServiceInterface fli = new FlickrServiceInterface();
			BasicInfo flinfo = fli.getBasicInfo(params.getValue("flickrAccessToken"), params.getValue("flickrTokenSecret"));
			result.add("flickrUserid", flinfo.getUserid());
			result.add("flickrName", flinfo.getName());
			result.add("flickrPhotoUrl", flinfo.getPhotoUrl());
		}
				
		return result;
	}
	
	public InteropTransferObject getMessages(InteropTransferObject params) throws Exception
	{
		InteropTransferObject result = new InteropTransferObject();
		
		// OK, see which services need to be updated (have their messages retrieved)
		// Read the parameters
		
		ArrayList<Message> messages = new ArrayList<Message>();
		
		if (params.containsKey("facebookAccessToken")) {
			// Kick off facebook!
			FacebookServiceInterface fbi = new FacebookServiceInterface();
			Message[] fbMessages = fbi.getMessages(params.getValue("facebookAccessToken"), null);
			for (int i = 0; i < fbMessages.length; i++)
				messages.add(fbMessages[i]);
		}
		
		if (params.containsKey("twitterAccessToken")) {
			// Check out twitter!
			TwitterServiceInterface twi = new TwitterServiceInterface();
			Message[] twMessages = twi.getMessages(params.getValue("twitterAccessToken"), 
					params.getValue("twitterTokenSecret"));
			
			for (int i = 0; i < twMessages.length; i++)
				messages.add(twMessages[i]);
		}

		if (params.containsKey("linkedinAccessToken")) {
			// Check out linkedin!
			LinkedinServiceInterface lii = new LinkedinServiceInterface();
			Message[] liMessages = lii.getMessages(params.getValue("linkedinAccessToken"), 
					params.getValue("linkedinTokenSecret"));
			
			for (int i = 0; i < liMessages.length; i++)
				messages.add(liMessages[i]);
		}
		
		if (params.containsKey("flickrAccessToken")) {
			// Check out flickr!
			// No don't!
		}
		
		// Prepare the results
		int size = messages.size();
		String[] sp = new String[size];
		String[] message = new String[size];
		String[] timestamp = new String[size];
		String[] messageId = new String[size];
		
		for (int i = 0; i < size; i++)
		{
			Message m = messages.get(i);
			sp[i] = Utils.getServiceProviderName(m.getServiceProvider(), true);
			message[i] = m.getMessage();
			timestamp[i] = Utils.getTimestamp(m.getTimestamp());
			messageId[i] = m.getId();
		}
		
		result.add("sp", sp);
		result.add("message", message);
		result.add("timestamp", timestamp);
		result.add("messageId", messageId);
		
		return result;
	}	
}