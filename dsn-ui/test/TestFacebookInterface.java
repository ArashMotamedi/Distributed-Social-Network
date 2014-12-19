import dsn.commons.development.Debug;
import dsn.commons.oauth.FlickrAuthUtils;
import dsn.ui.serviceInterface.BasicInfo;
import dsn.ui.serviceInterface.FacebookServiceInterface;
import dsn.ui.serviceInterface.FlickrServiceInterface;
import dsn.ui.serviceInterface.LinkedinServiceInterface;
import dsn.ui.serviceInterface.TwitterServiceInterface;


public class TestFacebookInterface {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		FacebookServiceInterface fbi = new FacebookServiceInterface();
//		BasicInfo bi = fbi.getBasicInfo("160295784005665|e418328f71139a8478896ad6-516938108|freFMIKkPAOrEohX0r5k7wZXR-A", "");
//		
//		LinkedinServiceInterface lii = new LinkedinServiceInterface();
//		bi = lii.getBasicInfo("6805e7f4-59fc-46b1-881c-2c25104c0f87", "2db6bfc3-96e1-44c5-8b80-f10d49e1d129");
//
//		TwitterServiceInterface twi = new TwitterServiceInterface(); 
//		bi = twi.getBasicInfo("18650637-Znrvry77zXPZhbKW0qog8IZsVynZe9am7gpRLWhq9", "QE7qXkpeZiUocZaprzqRABV9mG3BH9jnIYCvCetiA");
		
		FlickrServiceInterface fli = new FlickrServiceInterface();
		BasicInfo bi = fli.getBasicInfo("72157625415350694-ce4e13455fd73b0b", "");

	}

}
