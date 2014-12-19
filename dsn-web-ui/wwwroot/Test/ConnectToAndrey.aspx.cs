using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class Test_ConnectToAndrey : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

		InteropConsumer ic = new InteropConsumer("http://mbp:8080/dsn-dm-indexer");
		InteropTransferObject param = new InteropTransferObject();
		param.add("message", "hello Andrey!");
		InteropTransferObject to = ic.consumeService("updateIndex", param);
		Response.Write(to.getValue("message"));

/*		InteropTransferObject param = new InteropTransferObject();
		param.add("userId", "hippostoes");
		param.add("count", "10");

		InteropTransferObject to = ic.consumeService("getSimilarUsers", param);

		String[] users = to.getValues("similarUsers");
		for (int i = 0; i < users.Count(); i++)
		{
			Response.Write(users[i] + "\n");
		}

*/
		// Response.Write(to.getValue("message"));
    }
}