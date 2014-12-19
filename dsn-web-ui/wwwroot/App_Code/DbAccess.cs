using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Collections.Generic;

using MySql.Data.MySqlClient;


/// <summary>
/// Summary description for DbAccess
/// </summary>
public class DbAccess
{
	const string connectionString = "server=localhost;User Id=root;Password=aramarmarash;database=dsn";
	//const string connectionString = "server=dsn295b.db.5476382.hostedresource.com;User Id=dsn295b;Password=dsnPassw0rd;database=dsn295b";
	
	MySqlConnection conn = new MySqlConnection();

	public DbAccess()
	{
		conn.ConnectionString = connectionString;
		conn.Open();
	}

	private string dbSafeString(string str)
	{
		return str.Replace("'", "''");
	}

	private void executeNonquery(string command)
	{
		MySqlCommand comm = new MySqlCommand(command, conn);
		comm.ExecuteNonQuery();
	}

	private List<Dictionary<string, string>> executeReader(string command)
	{
		MySqlCommand comm = new MySqlCommand(command, conn);
		MySqlDataReader dr = comm.ExecuteReader();

		List<Dictionary<string, string>> results = new List<Dictionary<string, string>>();

		// any results?
		try
		{
			if (dr.Read())
			{
				// Get the field names
				string[] fieldNames = new string[dr.FieldCount];
				for (int i = 0; i < dr.FieldCount; i++)
				{
					fieldNames[i] = dr.GetName(i);
				}

				// Get the rows and insert them
				do
				{
					Dictionary<string, string> row = new Dictionary<string, string>(fieldNames.Length);
					for (int i = 0; i < fieldNames.Length; i++)
					{
						row.Add(fieldNames[i], Convert.ToString(dr[i]));
					}

					// Add this row to the results
					results.Add(row);
				} while (dr.Read());

			}

		}
		finally
		{
			dr.Close();
		}

		// return whatever came!
		return results;
	}


	public UserAccount authenticate(string email, string password)
	{
		// Check the email and password against the database
		List<Dictionary<string, string>> r = executeReader("SELECT * FROM UserAccounts WHERE Email = '" + dbSafeString(email) + "'");

		UserAccount account = new UserAccount();

		if (r.Count == 0)
		{
			// This email is not registered yet
			// So, register the user!

			try
			{
				executeNonquery("INSERT INTO UserAccounts (Email, Password) VALUES ('" +
					dbSafeString(email) + "', '" + dbSafeString(password) + "')");

			}
			catch (Exception ex)
			{
				throw new UserException("There was a problem with your email or password. Please try again.", ex);
			}

			// Recursively, reauthenticate this user
			return authenticate(email, password);
		}
		
		// Ok, so, the email exists. Is the password correct?
		if (r[0]["Password"] != password)
		{
			// Incorrect password
			throw new UserException("Invalid email or password was specified. Please try again.", null);
		}

		// Ok, looks like we have a properly authenticated user! Populate the account
		return getAccount(Convert.ToInt32(r[0]["UserId"]));
	}

	public UserAccount getAccount(int userId)
	{
		// Check the email and password against the database
		List<Dictionary<string, string>> r = executeReader("SELECT * FROM UserAccounts WHERE UserId = " + userId);
		UserAccount account = null;

		if (r.Count > 0)
		{
			account = new UserAccount();

			// Ok, looks like we have a properly authenticated user! Populate the account
			account.userId = Convert.ToInt32(r[0]["UserId"]);
			account.email = r[0]["Email"];
			account.password = r[0]["Password"];
			account.nameChoice = r[0]["NameServiceChoice"];
			account.imageChoice = r[0]["ImageServiceChoice"];


			// Now the accounts
			account.facebookUserId = r[0]["FacebookUserId"];
			account.facebookAccessToken = r[0]["FacebookAccessToken"];
			account.facebookTokenSecret = r[0]["FacebookTokenSecret"];
			account.facebookName = r[0]["FacebookName"];
			account.facebookImageUrl = r[0]["FacebookImageUrl"];
			account.facebookLink = r[0]["FacebookLink"];

			account.twitterUserId = r[0]["TwitterUserId"];
			account.twitterAccessToken = r[0]["TwitterAccessToken"];
			account.twitterTokenSecret = r[0]["TwitterTokenSecret"];
			account.twitterName = r[0]["TwitterName"];
			account.twitterImageUrl = r[0]["TwitterImageUrl"];
			account.twitterLink = r[0]["TwitterLink"];

			account.linkedinUserId = r[0]["LinkedinUserId"];
			account.linkedinAccessToken = r[0]["LinkedinAccessToken"];
			account.linkedinTokenSecret = r[0]["LinkedinTokenSecret"];
			account.linkedinName = r[0]["LinkedinName"];
			account.linkedinImageUrl = r[0]["LinkedinImageUrl"];
			account.linkedinLink = r[0]["LinkedinLink"];

			account.flickrUserId = r[0]["FlickrUserId"];
			account.flickrAccessToken = r[0]["FlickrAccessToken"];
			account.flickrTokenSecret = r[0]["FlickrTokenSecret"];
			account.flickrName = r[0]["FlickrName"];
			account.flickrImageUrl = r[0]["FlickrImageUrl"];
			account.flickrLink = r[0]["FlickrLink"];


			// Decide on the name!
			if (account.nameChoice == "fb")
				account.name = account.facebookName;
			else if (account.nameChoice == "li")
				account.name = account.linkedinName;
			else if (account.nameChoice == "tw")
				account.name = account.twitterName;
			else if (account.nameChoice == "fl")
				account.name = account.flickrName;

			// And on the image!
			if (account.imageChoice == "fb")
				account.image = account.facebookImageUrl;
			else if (account.imageChoice == "li")
				account.image = account.linkedinImageUrl;
			else if (account.imageChoice == "tw")
				account.image = account.twitterImageUrl;
			else if (account.imageChoice == "fl")
				account.image = account.flickrImageUrl;
		}

		return account;
	}

	public void registerToken(int userId, string serviceProvider, string token, string secret, string spUsername, string spName, string spImageUrl, string spLink)
	{
		string serviceAccess = "";
		string serviceSecret = "";
		string serviceUsername = "";
		string serviceName = "";
		string serviceImageUrl = "";
		string serviceLink = "";

		string prefix = "";
		string shortSp = "";

		switch (serviceProvider)
		{
			case "FACEBOOK":
				prefix = "Facebook";
				shortSp = "fb";
				break;
			case "TWITTER":
				prefix = "Twitter";
				shortSp = "tw";
				break;
			case "LINKEDIN":
				prefix = "Linkedin";
				shortSp = "li";
				break;
			case "FLICKR":
				prefix = "Flickr";
				shortSp = "fl";
				break;
		}

		serviceAccess = prefix + "AccessToken";
		serviceSecret = prefix + "TokenSecret";
		serviceUsername = prefix + "UserId";
		serviceName = prefix + "Name";
		serviceImageUrl = prefix + "ImageUrl";
		serviceLink = prefix + "Link";

		string command = "UPDATE UserAccounts SET " + serviceAccess + " = '" + dbSafeString(token) + "', " + 
			serviceSecret + " = '" + dbSafeString(secret) + "', " +
			serviceUsername + " = '" + dbSafeString(spUsername) + "', " +
			serviceName + " = '" + dbSafeString(spName) + "', " +
			serviceLink + " = '" + dbSafeString(spLink) + "', " +
			serviceImageUrl + " = '" + dbSafeString(spImageUrl) + "', " +
			"NameServiceChoice = IF(NameServiceChoice IS NULL, '" + shortSp + "', NameServiceChoice), " +
			"ImageServiceChoice = IF(ImageServiceChoice IS NULL, '" + shortSp + "', ImageServiceChoice) " +
			"WHERE UserId = " + userId;

		executeNonquery(command);
	}
}