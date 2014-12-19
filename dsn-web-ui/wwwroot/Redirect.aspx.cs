using System;
using System.Collections.Generic;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class Redirect : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
		Session["userid"] = Request.QueryString["userid"];
		Session["token"] = Request.QueryString["t"];
		Session["secret"] = Request.QueryString["s"];
		Response.Redirect(Request.QueryString["url"]);
    }
}