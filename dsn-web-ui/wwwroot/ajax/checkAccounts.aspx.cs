using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class ajax_checkAccounts : System.Web.UI.Page
{
	UserAccount account;
	protected void Page_Init(object sender, EventArgs e)
	{
		// This script needs authentication
		account = Utils.requireAuthentication(true);
	}

	protected void Page_Load(object sender, EventArgs e)
    {
		// Ok, update the account
		DbAccess da = new DbAccess();
		Session["UserAccount"] = da.getAccount(account.userId);

		// Prepare the output
		string result = "{\"facebookUserId\":\"" + account.facebookUserId + "\"," +
			"\"twitterUserId\":\"" + account.twitterUserId + "\"," +
			"\"linkedinUserId\":\"" + account.linkedinUserId + "\"," +
			"\"flickrUserId\":\"" + account.flickrUserId + "\"}";

		Response.Write(result);
    }
}