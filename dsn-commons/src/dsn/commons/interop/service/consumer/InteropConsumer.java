package dsn.commons.interop.service.consumer;

import java.net.URL;

import org.apache.axis.SimpleChain;
import org.apache.axis.configuration.BasicClientConfig;
import org.apache.axis.transport.http.CommonsHTTPSender;

import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;

public class InteropConsumer {
	private String endpoint = "";
	private static int requestId = 0;

	public InteropConsumer(String providerEndpoint) {
		endpoint = providerEndpoint;
	}

	public InteropTransferObject consumeService(String methodName,
			InteropTransferObject methodParams) throws Exception {
		// Increment requestID
		requestId += 1;

		// Prepare a SOAP 1.1 web service client
		BasicClientConfig clientConfig = new BasicClientConfig();
		SimpleChain simpleChain = new SimpleChain();
		simpleChain.addHandler(new CommonsHTTPSender());
		clientConfig.deployTransport("http", simpleChain);

		InteropTransferObject result = null;

		// Create a web service consumer proxy
		GeneralRemoteMethodServiceLocator consumer = new GeneralRemoteMethodServiceLocator(
				clientConfig);
		try {
			GeneralRemoteMethod m = consumer
					.getGeneralRemoteMethodPort(new URL(endpoint));

			Debug.log("Invoking " + methodName + " for Request ID " + requestId
					+ " on " + endpoint);
			result = InteropTransferObject.deserialize(m.invokeRemoteMethod(
					requestId, methodName, methodParams.serialize()));

			// Was there an error?!
			String errorMessage = null;
			try {
				errorMessage = result.getValue("ERROR");
			} catch (Exception ex) {
			}

			if (errorMessage != null)
				throw new Exception(errorMessage);

		} catch (Exception ex) {
			throw ex;
		}

		return result;
	}
}
