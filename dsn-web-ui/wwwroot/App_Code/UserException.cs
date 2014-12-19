using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;

/// <summary>
/// Summary description for UserException
/// </summary>
public class UserException : Exception
{
	public UserException(string message, Exception innerException) : base(message, innerException) 
	{ 
		// Log this error!
		string logLine = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + " - " + message;
		if (innerException != null)
			logLine += " - " + innerException.Message;

		logLine += "\r\n";

		File.AppendAllText(HttpContext.Current.Server.MapPath("/App_Data/errors.txt"), logLine);
	}
}