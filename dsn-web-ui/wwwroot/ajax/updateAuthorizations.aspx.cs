using System;
using System.Collections.Generic;
using System.Web;
using System.Net;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class ajax_updateAuthorizations : System.Web.UI.Page
{
	UserAccount account;
	protected void Page_Init(object sender, EventArgs e)
	{
		// This script needs authentication
		account = Utils.requireAuthentication(true);
	}

    protected void Page_Load(object sender, EventArgs e)
    {
		// Retrieve the text from authorization log
		WebClient wc = new WebClient();
		DbAccess da = new DbAccess();

		try
		{
			string authlog = wc.DownloadString("http://www.klipbord.com/GetAuthLog.aspx");

			if (authlog.Length > 0)
			{

				string[] authRecords = authlog.Replace("\r", "").Split(new char[] { '\n' }, StringSplitOptions.RemoveEmptyEntries);
				InteropTransferObject param = new InteropTransferObject();
				param.add("authRecords", authRecords);

				InteropConsumer ic = new InteropConsumer((string)Application["UIServiceUrl"]);
				InteropTransferObject result = ic.consumeService("updateAuthorizations", param);

				// Ok, now we have all the access tokens and token secrets!
				// Store them in the database!

				string[] userid = result.getValues("userid");
				string[] service = result.getValues("service");
				string[] accessToken = result.getValues("token");
				string[] tokenSecret = result.getValues("secret");
				string[] sp_Username = result.getValues("spUsername");
				string[] sp_Name = result.getValues("spName");
				string[] sp_Image = result.getValues("spImageUrl");
				string[] sp_Link = result.getValues("spLink");

				for (int i = 0; i < userid.Length; i++)
				{
					da.registerToken(Convert.ToInt32(userid[i]), service[i], accessToken[i], tokenSecret[i], sp_Username[i], sp_Name[i], sp_Image[i], sp_Link[i]);
				}

				// Delete the auth log
				wc.DownloadString("http://www.klipbord.com/DeleteAuthLog.aspx");
			}

		}
		catch
		{
			// If there was a problem, don't delete the log!
		}
    }
}