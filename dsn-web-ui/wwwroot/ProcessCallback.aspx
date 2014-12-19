<%@ Page Language="C#" AutoEventWireup="true" CodeFile="ProcessCallback.aspx.cs" Inherits="ProcessCallback" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Please wait...</title>

	<asp:PlaceHolder ID="phFacebookRedirector" Visible="false" runat="server">
	<script type="text/javascript">
		var newLoc = "" + window.location;
		newLoc = newLoc.replace("#", "&");
		window.location = newLoc;
	</script>
	</asp:PlaceHolder>

	<asp:PlaceHolder ID="phWindowCloser" Visible="true" runat="server">
	<script type="text/javascript">
		window.close();
	</script>
	</asp:PlaceHolder>
</head>
<body>
</body>
</html>
