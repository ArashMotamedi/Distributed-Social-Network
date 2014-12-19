using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class ajax_authenticate : System.Web.UI.Page
{
	string email, password;
	protected void Page_Init(object sender, EventArgs e)
	{ 
		// This script doesn't need authentication
	}

    protected void Page_Load(object sender, EventArgs e)
    {
		// Retrieve email and password and take appropriate action
		try
		{
			email = Request.Form["email"];
			password = Request.Form["password"];

			DbAccess db = new DbAccess();
			UserAccount account = db.authenticate(email, password);

			Session["UserAccount"] = account;

			// Does the user have any service providers specified?
			if (account.noServiceProviders)
			{
				Response.Write("SUCCESS:/Account.aspx");
			}
			else
			{
				Response.Write("SUCCESS:/Home.aspx");
			}
		}
		catch (UserException ex)
		{
			Response.Write("ERROR:" + ex.Message);
			return;
		}
		catch (Exception ex)
		{
			new UserException("# UNCAUGHT EXCEPTION #", ex);
			Response.Write("ERROR:There was an unexpected error. Please try again.");
			return;
		}
	}
}