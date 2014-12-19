import org.apache.axis.SimpleChain;
import org.apache.axis.configuration.BasicClientConfig;
import org.apache.axis.transport.http.CommonsHTTPSender;

import dsn.commons.development.Debug;
import dsn.commons.interop.service.consumer.GeneralRemoteMethod;
import dsn.commons.interop.service.consumer.GeneralRemoteMethodServiceLocator;

public class test {
	public static void main(String[] args) {

		// Prepare a SOAP 1.1 web service client
		BasicClientConfig clientConfig = new BasicClientConfig();
		SimpleChain simpleChain = new SimpleChain();
		simpleChain.addHandler(new CommonsHTTPSender());
		clientConfig.deployTransport("http", simpleChain);

		// Create a web service consumer proxy
		GeneralRemoteMethodServiceLocator consumer = new GeneralRemoteMethodServiceLocator(
				clientConfig);
		try {
			GeneralRemoteMethod m = consumer.getGeneralRemoteMethodPort();

			Debug.log("Making the first call");
			String resp = m.invokeRemoteMethod(0, "salam", "chetori:khoobam");
			System.out.println(resp);
			Debug.log("Finished with the first call");

			Debug.log("Making the second call");
			resp = m.invokeRemoteMethod(1, "salam", "chetori:kheyli+aali!");
			System.out.println(resp);
			Debug.log("Finished with the second call");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}