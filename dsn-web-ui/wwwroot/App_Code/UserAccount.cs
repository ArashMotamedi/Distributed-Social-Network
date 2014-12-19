using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for UserAccount
/// </summary>
public class UserAccount
{
	public string email;
	public string password;
	public int userId;
	public string nameChoice;
	public string imageChoice;

	public string name;
	public string image;

	public bool noServiceProviders
	{
		get
		{
			// Are all service user id's null?
			return (facebookUserId == "" && twitterUserId == "" & linkedinUserId == "" & flickrUserId == "");
		}
	}

	public string facebookUserId;
	public string facebookAccessToken;
	public string facebookTokenSecret;
	public string facebookName;
	public string facebookImageUrl;
	public string facebookLink;

	public string twitterUserId;
	public string twitterAccessToken;
	public string twitterTokenSecret;
	public string twitterName;
	public string twitterImageUrl;
	public string twitterLink;

	public string linkedinUserId;
	public string linkedinAccessToken;
	public string linkedinTokenSecret;
	public string linkedinName;
	public string linkedinImageUrl;
	public string linkedinLink;


	public string flickrUserId;
	public string flickrAccessToken;
	public string flickrTokenSecret;
	public string flickrName;
	public string flickrImageUrl;
	public string flickrLink;

}