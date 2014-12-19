package dsn.bots;

import dsn.commons.Utils;
import dsn.commons.development.Debug;

public class TestFacebook {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String access = "160295784005665|9a52297327c7b0733b6021ff-100000117124181|dP0NXgYKnn-jW_ArAgrQbCccy4Q";
		String access = "160295784005665|e418328f71139a8478896ad6-516938108|freFMIKkPAOrEohX0r5k7wZXR-A";
		String url = "https://graph.facebook.com/me/statuses";
		
		String signedUrl = url + "?access_token=" + access;
		String response = Utils.getUrl(signedUrl);
		Debug.log(response);

	}

}
