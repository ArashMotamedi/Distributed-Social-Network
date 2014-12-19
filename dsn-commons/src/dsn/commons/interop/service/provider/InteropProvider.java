package dsn.commons.interop.service.provider;

import javax.xml.ws.Endpoint;

import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;
import dsn.commons.development.Debug;

public class InteropProvider {

	// public InteropServices() {
	public InteropProvider(String serviceUrl, InteropHandler interopHandler) {
		Debug.logInfo("Initializing the web service provider");
		ConfigSet configs = new ConfigSet(ConfigType.INTEROP);

		// Define a service URL, and instantiate a service object and an
		// endpoint
		GeneralRemoteMethod serviceInstance = new GeneralRemoteMethod(
				interopHandler);
		Endpoint serviceEndpoint = Endpoint.create(serviceInstance);

		try {
			// Try to publish the service interface
			serviceEndpoint.publish(serviceUrl);
		} catch (Exception ex) {
			// Log the error
			Debug.logError(ex);
			Debug.logInfo("Restart JVM to kill previously deployed web services");
			Debug.logInfo("Exiting with return code 1");

			// Force a system exit
			System.exit(1);
		}

		// If everything went well, then show a success message
		// and instructions on how to stop the server
		Debug.logInfo("Web service successfully deployed at " + serviceUrl);
		System.out.println("Press Ctrl+C to stop the web service");
	}
}
