import dsn.commons.development.Debug;
import dsn.commons.oauth.OAuthUtils;

public class testoauth {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Debug.log("Salam");
		OAuthUtils oa = new OAuthUtils();
		String r = "";
		// try {
		// r = oa.getAuthenticationUrl(ServiceProvider.LINKEDIN, 0);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Debug.log(r);
	}

}
