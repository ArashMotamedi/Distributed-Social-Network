using System;
using System.IO;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class ProcessCallback : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
		// If the callback is from facebook, you gotta dance a little bit!
		if (Request.QueryString["src"] == "fb" && Request.QueryString["access_token"] == null)
		{
			phFacebookRedirector.Visible = true;
			phWindowCloser.Visible = false;
			return;
		}

		File.AppendAllText(Server.MapPath("/App_Data/auth.log"), Session["userid"] + ":" + Session["token"] + ":" + Session["secret"] + ":" + Request.Url.Query.Substring(1) + "\n");
    }
}