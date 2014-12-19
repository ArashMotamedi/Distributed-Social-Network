using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for Utils
/// </summary>
public static class Utils
{
	public static UserAccount requireAuthentication(bool AjaxCall)
	{
		if (HttpContext.Current.Session["UserAccount"] == null)
		{
			if (AjaxCall)
			{
				HttpContext.Current.Response.Write("REDIRECT:/");
				HttpContext.Current.Response.End();
			}
			else
			{
				setSessionMessage("Log in!", "Please log in for access to OneStop features.", true);
				HttpContext.Current.Response.Redirect("/");
				HttpContext.Current.Response.End();
			}
		}

		return (UserAccount)HttpContext.Current.Session["UserAccount"];
	}

	public static void setSessionMessage(string title, string body, bool isError)
	{
		HttpContext.Current.Session["MessageTitle"] = title;
		HttpContext.Current.Session["MessageBody"] = body;
		HttpContext.Current.Session["MessageIsError"] = isError;
	}

	public static UserAccount getSessionAccount()
	{
		return (UserAccount)HttpContext.Current.Session["UserAccount"];
	}
}