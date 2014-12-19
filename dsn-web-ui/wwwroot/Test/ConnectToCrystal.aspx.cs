using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class Test_ConnectToCrystal : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
		InteropConsumer ic = new InteropConsumer("http://192.168.1.104:8080/dsn-interop");
		InteropTransferObject to = ic.consumeService("helloWorldScheduler", new InteropTransferObject());
		Response.Write(to.getValue("message"));
    }
}