package dsn.scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import dsn.bots.facebook.FacebookBot;
import dsn.bots.flickr.FlickrBot;
import dsn.bots.linkedin.LinkedinBot;
import dsn.bots.twitter.TwitterBot;

public class DSNJobHelper extends DBInfo {
	public static FlickrBot flickrBot;
	public static TwitterBot twitterBot;
	public static FacebookBot facebookBot;
	public static LinkedinBot linkedinBot;
	public static DOMXML domxml;
	public static CalendarHelper calendarHelper;
	public static DBInfo dbInfo;
	public static UpdateIndexHelper updateIndexHelper;
	public static UserTokens userTokens;
	private String[] passThisUser;

	public DSNJobHelper() {

	}

	public void runScheduler(int intervalInMin) throws Exception {
		while (true) {
			this.passDataAllUsers(intervalInMin);

			try {
				Thread.sleep(intervalInMin);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void passDataAllUsers(int intervalInMin) throws Exception {
		domxml = new DOMXML();
		calendarHelper = new CalendarHelper();
		userTokens = new UserTokens();

		Integer currentDateTime[] = calendarHelper.getCurrentDateTime();

		int year = currentDateTime[0];
		int month = currentDateTime[1];
		int day = currentDateTime[2];
		int hour24 = currentDateTime[3];
		int min = currentDateTime[4];
		int sec = currentDateTime[5];
		// System.out.println("hour:"+hour24+"min:"+min+"sec:"+sec);
		userTokens = super.getAccounts(intervalInMin);

		String[][] users = userTokens.users;
		String[] tokens = userTokens.tokens;
		String[] secrets = userTokens.secrets;

		for (int i = 0; i < users.length; i++) {
			String[] passThisUser = { users[i][0], users[i][1], users[i][2],
					users[i][3], users[i][4] };
			// sets the schedule for when to run bot: hour, minute, second
			DSNJob dsnJob = new DSNJob(hour24, min, sec + 2);
			dsnJob.start(passThisUser, tokens, secrets, 0, currentDateTime,
					intervalInMin);
		}
	}

	/*
	 * String thistime = updateIndexHelper.lastIndexRun();
	 * System.out.println(thistime);
	 * DataMiningInterface.main(updateIndexHelper.lastIndexRun());
	 */

	// called when UI requests to add or update a user
	public void passDataThisUser(String user_id) throws Exception {
		domxml = new DOMXML();
		calendarHelper = new CalendarHelper();
		updateIndexHelper = new UpdateIndexHelper();

		Integer currentDateTime[] = calendarHelper.getCurrentDateTime();

		int year = currentDateTime[0];
		int month = currentDateTime[1];
		int day = currentDateTime[2];
		int hour24 = currentDateTime[3];
		int min = currentDateTime[4];
		int sec = currentDateTime[5];

		userTokens = super.getAccountsThisUser(user_id);
		String[] thisUserAccount = userTokens.thisUserAccount;
		String[] tokens = userTokens.tokens;
		String[] secrets = userTokens.secrets;

		// System.out.println("hour:"+hour24+"min:"+min+"sec:"+sec);
		// String[] passThisUser = super.getAccountsThisUser(user_id);
		// String[] passThisUser = { users[0][0], users[0][1], users[0][2],
		// users[0][3], users[0][4] };
		// String[] passThisUser = thisUserAccount;

		DSNJob dsnJob = new DSNJob(hour24, min, sec + 2);
		dsnJob.start(thisUserAccount, tokens, secrets, 1, currentDateTime, 0);
		//System.out.println("===="+thisUserAccount[0] + "===="+tokens[1] +
		//"===="+secrets[1]);
		DataMiningInterface.main(updateIndexHelper.lastIndexRun());
		// formatDateTime(datestamp);
	}

	public void deleteRawData(String[] users) throws IOException {

		if (!users[1].equals("0")) {
			this.deleteRawDataFacebook(users[1]);
		} // facebook
		if (!users[2].equals("0")) {
			this.deleteRawDataFlickr(users[2]);
		} // flickr
		if (!users[3].equals("0")) {
			this.deleteRawDataTwitter(users[3]);
		} // twitter
		if (!users[4].equals("0")) {
			this.deleteRawDataLinkedin(users[4]);
		} // linkedin
	}

	public void deleteRawDataFacebook(String user) throws IOException {
		facebookBot = new FacebookBot();
		File facebookFilename = new File(facebookBot.getXmlFilename(user));

		boolean success = facebookFilename.delete();

		if (success) {
			System.out.println("Succesfully deleted file: " + facebookFilename);
		} else {
			System.out.println("Error deleting file: " + facebookFilename);
		}
	}

	public void deleteRawDataFlickr(String user) throws IOException {
		flickrBot = new FlickrBot();
		File flickrFilename = new File(flickrBot.getXmlFilename(user));

		boolean success = flickrFilename.delete();

		if (success) {
			System.out.println("Succesfully deleted file: " + flickrFilename);
		} else {
			System.out.println("Error deleting file: " + flickrFilename);
		}
	}

	public void deleteRawDataTwitter(String user) throws IOException {
		twitterBot = new TwitterBot();

		File twitterFilename = new File(twitterBot.getXmlFilename(user));

		boolean success = twitterFilename.delete();

		if (success) {
			System.out.println("Succesfully deleted file: " + twitterFilename);
		} else {
			System.out.println("Error deleting file: " + twitterFilename);
		}
	}

	public void deleteRawDataLinkedin(String user) throws IOException {
		linkedinBot = new LinkedinBot();

		File linkedinBotFilename = new File(linkedinBot.getXmlFilename(user));

		boolean success = linkedinBotFilename.delete();

		if (success) {
			System.out.println("Succesfully deleted file: "
					+ linkedinBotFilename);
		} else {
			System.out.println("Error deleting file: " + linkedinBotFilename);
		}
	}
}
