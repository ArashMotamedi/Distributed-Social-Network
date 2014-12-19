package dsn.commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

import dsn.commons.development.Debug;
import dsn.commons.oauth.OAuthUtils;

public class Utils {
	
	public enum ServiceProvider {
		LINKEDIN, TWITTER, FLICKR, FACEBOOK
	}
	
	public static ServiceProvider getServiceProvider(String serviceProviderName){
		ServiceProvider result = null;
		String sp = serviceProviderName.toLowerCase();
		
		if (sp.equals("fb") || sp.equals("facebook"))
			result = ServiceProvider.FACEBOOK;
		else if (sp.equals("li") || sp.equals("linkedin"))
			result = ServiceProvider.LINKEDIN;
		else if (sp.equals("tw") || sp.equals("twitter"))
			result = ServiceProvider.TWITTER;
		else if (sp.equals("fl") || sp.equals("flickr"))
			result = ServiceProvider.FLICKR;
		
		return result;
	}
	
	public static String getServiceProviderName(ServiceProvider serviceProvider, Boolean shortName)
	{
		String name = "", nameshort = "";
		switch (serviceProvider) {
		case FACEBOOK:
			name = "Facebook";
			nameshort = "fb";
			break;
		case TWITTER:
			name = "Twitter";
			nameshort = "tw";
			break;
		case LINKEDIN:
			name = "Linkedin";
			nameshort = "li";
			break;
		case FLICKR:
			name = "Flickr";
			nameshort = "fl";
			break;
		}
		
		if (shortName)
			return nameshort;
		
		return name;		
	}
	
	public static String getServiceProviderName(String serviceProviderName, Boolean shortName)
	{
		return getServiceProviderName(getServiceProvider(serviceProviderName), shortName);
	}
	
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception ex) {
			return "";
		}
	}

	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, "UTF-8");
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getQueryValue(String query, String key) {
		// To help with the search!
		String q = "&" + query + "&";
		String k = "&" + key + "=";

		String result = "";
		try {
			result = urlDecode(q.substring(q.indexOf(k) + k.length(),
					q.indexOf("&", q.indexOf(k) + 1)));
		} catch (Exception ex) {
		}
		return result;
	}
	
	public static String getXmlValue(String xml, String tag)
	{
		String result = "";
		
		String topen = "<" + tag + ">";
		String tclose = "</" + tag + ">";   
		
		try
		{
			result = xml.substring(xml.indexOf(topen) + topen.length(),
					xml.indexOf(tclose, xml.indexOf(topen) + 1));
		} catch (Exception ex) {}
		
		return result;
	}

	public static String getUrl(String url, ServiceProvider serviceProvider,
			String accessToken, String tokenSecret) {
		String result = "";
		HttpURLConnection connection = null;
		// OutputStreamWriter wr = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;

		URL serverAddress = null;
		
		try {
			serverAddress = new URL(url);
			// set up out communications stuff
			connection = null;

			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			connection.setInstanceFollowRedirects(false);

			// Is a service provider specified?
			if (serviceProvider != null) {
				// Then sign the thing!
				OAuthUtils oauth = new OAuthUtils();
				oauth.signRequest(connection, serviceProvider, accessToken,
						tokenSecret);
			}

			connection.connect();
			
			// Is it a normal response?
			if (connection.getResponseCode() == 200) {

				// read the result from the server
				rd = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				sb = new StringBuilder();

				while ((line = rd.readLine()) != null) {
					sb.append(line + '\n');
				}

				result = sb.toString();
			}
			else
			{
				// If the response type is image
				// Then report its location!
				if (connection.getResponseCode() == 302)
				{
					result = connection.getHeaderField("Location");
				}
				else if (connection.getContentType().startsWith("image"))
				{
					result = connection.getHeaderField("Location");
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the connection, set all objects to null
			connection.disconnect();
			rd = null;
			sb = null;
			connection = null;
		}

		return result;
	}

	public static String getUrl(String url) {
		// Unsigned request
		return getUrl(url, null, null, null);
	}
	
	public static Calendar getCalendar(ServiceProvider sp, String timestamp)
	{
		int year = 0, month = 0, date = 0, hour = 0, minute = 0, second = 0;
		String[] t;
		
		switch (sp) {
		case FACEBOOK:
			t = timestamp.split("[\\+T:-]");
			year = Integer.parseInt(t[0]);
			month = Integer.parseInt(t[1]);
			date = Integer.parseInt(t[2]);
			hour = Integer.parseInt(t[3]);
			minute = Integer.parseInt(t[4]);
			second = Integer.parseInt(t[5]);
			break;
			
		case TWITTER:
			t = timestamp.split("[:\\s]");
			year = Integer.parseInt(t[7]);
			month = getMonthOrder(t[1]);
			date = Integer.parseInt(t[2]);
			hour = Integer.parseInt(t[3]);
			minute = Integer.parseInt(t[4]);
			second = Integer.parseInt(t[5]);
			break;
		case LINKEDIN:
			Calendar ct = Calendar.getInstance();
			ct.set(1970, 1, 1, 0, 0, 0);
			int amount = (int)(Long.parseLong(timestamp) / 1000);
			ct.add(Calendar.SECOND, amount);
			year = ct.get(Calendar.YEAR);
			month = ct.get(Calendar.MONTH);
			date = ct.get(Calendar.DATE);
			hour = ct.get(Calendar.HOUR_OF_DAY);
			minute = ct.get(Calendar.MINUTE);
			second = ct.get(Calendar.SECOND);
			break;
		default:
			break;
		}
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, date, hour, minute, second);
		return c;
	}
	
	private static int getMonthOrder(String month)
	{
		String m = month.substring(0, 3).toLowerCase();
		int mon = 0; 
		if (m.equals("jan")) mon = 1;
		else if (m.equals("feb")) mon = 2;
		else if (m.equals("mar")) mon = 3;
		else if (m.equals("apr")) mon = 4;
		else if (m.equals("may")) mon = 5;
		else if (m.equals("jun")) mon = 6;
		else if (m.equals("jul")) mon = 7;
		else if (m.equals("aug")) mon = 8;
		else if (m.equals("sep")) mon = 9;
		else if (m.equals("oct")) mon = 10;
		else if (m.equals("nov")) mon = 11;
		else if (m.equals("dec")) mon = 12;
		
		return mon;
	}
	
	public static String getTimestamp(Calendar c)
	{
		String timestamp = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE) + " "
			+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
		return timestamp;
	}

}
