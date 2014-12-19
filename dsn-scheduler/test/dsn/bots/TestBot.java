package dsn.bots;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

//import json.JSONException;

import org.xml.sax.SAXException;

import dsn.bots.flickr.FlickrBot;
import dsn.bots.twitter.TwitterBot;
import dsn.bots.facebook.FacebookBot;
import dsn.scheduler.CalendarHelper;
import dsn.scheduler.DSNJobHelper;
import dsn.scheduler.DBInfo;

public class TestBot {

        public static FlickrBot flickrBot;
        public static TwitterBot twitterBot;
        public static FacebookBot facebookBot;
        public static DSNJobHelper dsnJobHelper;
        public static DBInfo dbInfo;
        
        public static String testUserTwitter = "cldulay0504";
        public static String testUserFlickr = "dsn_cldulay0504";
        public static String testUserFacebook2 = "100000117124181";     
        public static String testUserFacebook = "106463669422584";
        
        public static String[] TwitterUsers = { "cldulay0504", "gsv" };
        public static String[] TestUser = { "100000117124181", "dsn_cldulay0504", "cldulay0504" };
        public static String [][] TestUser2 = { 
        	{ "100000117124181", "dsn_cldulay0504", "cldulay0504" },
        	{ "0", "0", "gsv" },
        	{ "0", "0", "suzette280" },
        	{ "0", "0", "jojobellow" },
        	{ "0", "0", "cookster88" },
        	{ "629423053", "0", "jerreldulay" }
        };
        
        
        public TestBot() throws IOException {
                
        }

        public static void main(String[] args) throws Exception {
        	facebookBot = new FacebookBot();
        	flickrBot = new FlickrBot();
            twitterBot = new TwitterBot();
            dsnJobHelper = new DSNJobHelper();
            dbInfo = new DBInfo();
            CalendarHelper calendarHelper = new CalendarHelper();
            
            //facebookBot.runAll("14", "160295784005665|e418328f71139a8478896ad6-516938108|freFMIKkPAOrEohX0r5k7wZXR-A", null);
            //calendarHelper.formatTimestamp("2009-11-20T01:19:06+0000");
            //testFacebook();
			//flickrBot.runAll(testUserFlickr);
			//testFlickr();
			//twitterBot.runAll(testUserTwitter);
			//testTwitter();
			
        	//dsnJobHelper.passData(TestUser); // test all accounts per user
			//dsnJobHelper.passData(TestUser2); // all accounts for all users
            dsnJobHelper.passDataAllUsers(150000);
            //System.out.println(dbInfo.getUpdatedUniqueUserSize("2010-07-01 03:30:25"));
            //dsnJobHelper.passDataThisUser("17");
			//dbInfo.updateDateTime("2");
            //dbInfo.getAccounts();
			            
        }
        /*
        private static void testFacebook() throws JSONException, IOException {
        	FacebookBot facebookBot = new FacebookBot();
        	
        	System.out.println("\n==== TEST FACEBOOK ====");
        	System.out.println("facebook REAL_NAME: " + facebookBot.getRealName(facebookBot.getXmlFilename(testUserFacebook)));
        	System.out.println("facebook FIRST_NAME: " + facebookBot.getFirstName(facebookBot.getXmlFilename(testUserFacebook)));
        	System.out.println("facebook LAST_NAME: " + facebookBot.getLastName(facebookBot.getXmlFilename(testUserFacebook)));
        	System.out.println("facebook LOCALE: " + facebookBot.getLocale(facebookBot.getXmlFilename(testUserFacebook)));
        	System.out.println("facebook MESSGAE: " + facebookBot.getMessage(facebookBot.getXmlFilename(testUserFacebook)));
        	
        }
        private static void testFlickr() throws IOException {
        	FlickrBot flickrBot = new FlickrBot();
        	
        	System.out.println("\n==== TEST FLICKR ====");
        	System.out.println("flickr SCREEN_NAME: " + flickrBot.getScreenName(flickrBot.getXmlFilename(testUserFlickr)));
            System.out.println("flickr REAL_NAME: " + flickrBot.getRealName(flickrBot.getXmlFilename(testUserFlickr)));
            System.out.println("flickr USER_ID: " + flickrBot.getUserId(flickrBot.getXmlFilename(testUserFlickr)));
            System.out.println("flickr PHOTOS_COUNT: " + flickrBot.getPhotosCount(flickrBot.getXmlFilename(testUserFlickr)));
            
        }
        
        private static void testTwitter() throws IOException {
        	TwitterBot twitterBot = new TwitterBot();
        	
        	System.out.println("\n==== TEST TWITTER ====");

        	System.out.println("twitter SCREEN_NAME: " + twitterBot.getScreenName(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter REAL_NAME: " + twitterBot.getRealName(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter LOCATION: " + twitterBot.getLocation(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter DESCRIPTION: " + twitterBot.getDescription(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter USER_ID: " + twitterBot.getUserId(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter STATUS: " + twitterBot.getStatus(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter STATUS_COUNT: " + twitterBot.getStatusCount(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter FRIENDS_COUNT: " + twitterBot.getFriendsCount(twitterBot.getXmlFilename(testUserTwitter)));
            System.out.println("twitter PROFILE_IMAGE: " + twitterBot.getProfileImage(twitterBot.getXmlFilename(testUserTwitter)));

        }
*/
}