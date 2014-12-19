package dsn.scheduler;

import java.io.IOException;
import java.sql.SQLException;

import dsn.bots.facebook.FacebookBot;
import dsn.bots.flickr.FlickrBot;
import dsn.bots.twitter.TwitterBot;
import dsn.bots.linkedin.LinkedinBot;
import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;
import dsn.commons.interop.service.provider.InteropProvider;

public class SchedulerLauncher {

	public static FlickrBot flickrBot;
	public static TwitterBot twitterBot;
	public static FacebookBot facebookBot;
	public static LinkedinBot linkedinBot;
	public static DSNJobHelper dsnJobHelper;
	public static DBInfo dbInfo;
	public static UpdateIndexHelper updateIndexHelper;

	public static void main(String[] args) throws Exception {

		facebookBot = new FacebookBot();
		flickrBot = new FlickrBot();
		twitterBot = new TwitterBot();
		linkedinBot = new LinkedinBot();
		dsnJobHelper = new DSNJobHelper();
		dbInfo = new DBInfo();
		updateIndexHelper = new UpdateIndexHelper();

		InteropProvider ip = new InteropProvider("http://cdulaymb:8080/dsn-scheduler", 
				new SchedulerInteropHandler());
		
		dsnJobHelper.runScheduler(300000); // 5 minutes
		//dsnJobHelper.runScheduler(150000);
		//dbInfo.checkUserAccountExists("11", "tw", "arash2", "li-token4", "li-secret3");
		// dsnJobHelper.passDataAllUsers();
		 
	}
}