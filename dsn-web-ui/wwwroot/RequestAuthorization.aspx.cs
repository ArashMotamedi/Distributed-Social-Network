using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class RequestAuthorization : System.Web.UI.Page
{
	UserAccount account;
	protected void Page_Init(object sender, EventArgs e)
	{
		// This script needs authentication
		Utils.requireAuthentication(false);
		account = Utils.getSessionAccount();
	}

	protected void Page_Load(object sender, EventArgs e)
	{
		// See which service is being requested
		string serviceProvider = Request.QueryString["serviceProvider"];

		// Facebook is simple
		if (serviceProvider == "fb")
		{
			Response.Redirect("http://www.klipbord.com/Redirect.aspx?userid=" + account.userId + "&url=" + Server.UrlEncode("https://graph.facebook.com/oauth/authorize?client_id=160295784005665&redirect_uri=http://www.klipbord.com/ProcessCallback.aspx%3fsrc=fb&type=user_agent&display=popup&scope=offline_access"));
			return;
		}


		InteropConsumer ic = new InteropConsumer((string)Application["UIServiceUrl"]);

		InteropTransferObject parameters = new InteropTransferObject();
		parameters.add("serviceProvider", serviceProvider);

		InteropTransferObject result =
			ic.consumeService("getAuthenticationUrl", parameters);

		string s = "", t = "", u = "";
		try
		{
			u = result.getValue("url");
			t = result.getValue("token");
			s = result.getValue("secret");
		}
		catch { }

		Response.Redirect("http://www.klipbord.com/Redirect.aspx?userid=" + account.userId + "&t=" +  t  + "&s=" + s   + "&url=" + Server.UrlEncode(u));
	}
}