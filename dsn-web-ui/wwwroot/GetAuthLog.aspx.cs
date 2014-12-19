using System;
using System.Collections.Generic;
using System.IO;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class GetAuthLog : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
		try
		{
			Response.Write(File.ReadAllText(Server.MapPath("/App_Data/auth.log")));
		}
		catch { }
    }
}