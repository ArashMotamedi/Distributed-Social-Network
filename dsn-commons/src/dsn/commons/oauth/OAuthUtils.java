package dsn.commons.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;
import dsn.commons.development.Debug;

public class OAuthUtils {
	private ConfigSet configs = new ConfigSet(ConfigType.OAUTH);

	// Internal use... for creating a dynamic oauth provider
	private OAuthProvider getOAuthProvider(ServiceProvider serviceProvider) {
		String requestTokenUrl = "", accessTokenUrl = "", authorizationUrl = "";

		requestTokenUrl = configs.getAttribute(serviceProvider.name()
				+ "requesttokenurl");
		accessTokenUrl = configs.getAttribute(serviceProvider.name()
				+ "accesstokenurl");
		authorizationUrl = configs.getAttribute(serviceProvider.name()
				+ "authorizationurl");

		OAuthProvider provider = new DefaultOAuthProvider(requestTokenUrl,
				accessTokenUrl, authorizationUrl);

		provider.setOAuth10a(true);

		return provider;
	}

	// Internal use... For creating a dynamic oauth consumer
	private OAuthConsumer getOAuthConsumer(ServiceProvider serviceProvider) {
		String consumerKey = "", secretKey = "";
		consumerKey = configs.getAttribute(serviceProvider.name()
				+ "consumerkey");
		secretKey = configs.getAttribute(serviceProvider.name() + "secretkey");

		OAuthConsumer consumer = new DefaultOAuthConsumer(consumerKey,
				secretKey);

		return consumer;
	}

	// Useful for signing a request with OAuth authorization
	public void signRequest(HttpURLConnection request,
			ServiceProvider serviceProvider, String accessToken,
			String tokenSecret) throws Exception {
		// OAuth signing for a request object
		OAuthConsumer consumer = getOAuthConsumer(serviceProvider);
		consumer.setTokenWithSecret(accessToken, tokenSecret);
		consumer.sign(request);
	}

	public String[] getAccessTokens(ServiceProvider serviceProvider,
			String token, String tokenSecret, String oauthVerifier)
			throws Exception {
		String[] result = null;
		try {
			OAuthConsumer consumer = getOAuthConsumer(serviceProvider);
			OAuthProvider provider = getOAuthProvider(serviceProvider);
			consumer.setTokenWithSecret(token, tokenSecret);

			provider.retrieveAccessToken(consumer, oauthVerifier);

			result = new String[] { consumer.getToken(),
					consumer.getTokenSecret() };
		} catch (Exception ex) {
			Debug.logError(ex);
		}

		return result;
	}

	// Find the URL where user can authorize our service
	public String[] getAuthenticationUrl(ServiceProvider serviceProvider)
			throws Exception {
		String[] result = null;
		String callbackUrl = "";

		// Get oauth consumer and provider
		callbackUrl = configs.getAttribute(serviceProvider.name()
				+ "callbackurl");

		try {
			OAuthConsumer consumer = getOAuthConsumer(serviceProvider);
			OAuthProvider provider = getOAuthProvider(serviceProvider);
			String url = provider.retrieveRequestToken(consumer, callbackUrl);
			result = new String[] { consumer.getToken(),
					consumer.getTokenSecret(), url };

		} catch (Exception e) {
		}

		return result;
	}

}