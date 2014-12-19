package dsn.ui.serviceInterface;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;
import dsn.commons.oauth.OAuthUtils;

public class LinkedinServiceInterface extends ServiceInterface {

	@Override
	public BasicInfo getBasicInfo(String accessToken, String tokenSecret) {
		
		BasicInfo result = new BasicInfo();
		String resp = Utils.getUrl(
				"http://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,public-profile-url)",
				ServiceProvider.LINKEDIN, accessToken, tokenSecret);
		
		// Name
		result.setName(Utils.getXmlValue(resp, "first-name") + " " + 
				Utils.getXmlValue(resp, "last-name"));
		
		// User ID on linkedin
		result.setUserid(Utils.getXmlValue(resp, "id"));

		// Picture Url
		String photoUrl = Utils.getXmlValue(resp, "picture-url");
		result.setPhotoUrl(photoUrl);
		result.setLargePhotoUrl(photoUrl);
		
		// Link
		result.setLink(Utils.getXmlValue(resp, "public-profile-url"));
		
		return result;
	}
	
	@Override 
	public String[] exchangeRequestWithAccess(String requestToken, String tokenSecret, String verifier) throws Exception
	{
		OAuthUtils oa = new OAuthUtils();
		return oa.getAccessTokens(ServiceProvider.LINKEDIN, requestToken, tokenSecret, verifier);
	}

	@Override
	public Message[] getMessages(String accessToken, String tokenSecret) throws Exception
	{
		Message[] messages = null;

		String resp = Utils.getUrl("http://api.linkedin.com/v1/people/~/network/updates?scope=self&type=SHAR",
				ServiceProvider.LINKEDIN, accessToken, tokenSecret);
		
		Document d = null;
		try
		{
			d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(resp)));
		} catch (Exception ex) {}
		
		NodeList nlTimestamp = d.getElementsByTagName("timestamp");
		NodeList nlUpdateKey = d.getElementsByTagName("update-key");
		NodeList nlStatus = d.getElementsByTagName("current-status");
		
		messages = new Message[nlTimestamp.getLength()];
		
		for (int i = 0; i < messages.length; i++)
		{
			messages[i] = new Message();
			messages[i].setServiceProvider(ServiceProvider.LINKEDIN);
			messages[i].setId(nlUpdateKey.item(i).getFirstChild().getNodeValue());
			messages[i].setTimestamp(Utils.getCalendar(ServiceProvider.LINKEDIN, nlTimestamp.item(i).getFirstChild().getNodeValue()));
			messages[i].setMessage(nlStatus.item(i).getFirstChild().getNodeValue());
		}		
		
		return messages;
	}
}
