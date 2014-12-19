package dsn.commons.oauth;

import java.math.BigInteger;
import java.security.MessageDigest;

import dsn.commons.Utils;
import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;

public class FlickrAuthUtils {
	ConfigSet configs = new ConfigSet(ConfigType.OAUTH);

	public String getSignedQuery(String query) {
		String resultQuery = "api_key="
				+ configs.getAttribute("flickrconsumerkey") + "&" + query;
		String signature = configs.getAttribute("flickrsecretkey")
				+ resultQuery.replaceAll("=", "").replaceAll("&", "");

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(signature.getBytes("UTF-8"));
			resultQuery += "&api_sig=" + new BigInteger(1, bytes).toString(16);
		} catch (Exception ex) {
		}

		return resultQuery;
	}

	public String getAuthenticationUrl() {
		String url = configs.getAttribute("flickrauthorizationurl");
		url += "?" + getSignedQuery("perms=read");

		return url;
	}

	public String getAccessToken(String frob) {
		String token = "";

		String url = configs.getAttribute("flickrrequesturl");
		url += "?"
				+ getSignedQuery("frob=" + frob
						+ "&method=flickr.auth.getToken");

		// Ok, send this!
		String resp = Utils.getUrl(url);

		try {
			token = Utils.getXmlValue(resp, "token");

		} catch (Exception ex) {
		}

		return token;
	}
}
