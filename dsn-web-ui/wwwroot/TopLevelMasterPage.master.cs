using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class TopLevelMasterPage : System.Web.UI.MasterPage
{
	UserAccount account = Utils.getSessionAccount();

	protected void Page_Init(object sender, EventArgs e)
	{
		if (Request.Url.LocalPath.ToLower() == "/default.aspx")
		{
			if (account != null)
			{
				Response.Redirect("/Logout.aspx");
				return;
			}
		}
		else
		{
			Utils.requireAuthentication(false);

			// Show the header shortcuts
			phShortcuts.Visible = true;

			litUserid.Text = account.userId.ToString();
			litName.Text = account.name;
			litEmail.Text = account.email;

			// Accounts list
			litFacebookUserId.Text = account.facebookUserId;
			litTwitterUserId.Text = account.twitterUserId;
			litLinkedinUserId.Text = account.linkedinUserId;
			litFlickrUserId.Text = account.flickrUserId;


			// Show the display name as well
			if (String.IsNullOrEmpty(account.name))
			{
				litDisplayName.Text = account.email;
			}
			else
			{
				litDisplayName.Text = account.name;
			}

		}
	}
	
	protected void Page_Load(object sender, EventArgs e)
    {		
		// Any session messages?
		if (Session["MessageTitle"] != null && Session["MessageBody"] != null && Session["MessageIsError"] != null)
		{
			litBodyLoad.Text = "showPageMessage(\"" + Session["MessageTitle"] + "\", \"" +
				Session["MessageBody"] + "\", " + Convert.ToString(Session["MessageIsError"]).ToLower() + ");";

			phMessage.Visible = true;
		}
		Session["MessageTitle"] = Session["MessageBody"] = Session["MessageIsError"] = null;
    }
}
