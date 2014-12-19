package dsn.commons.interop.service.provider;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;

@WebService
public class GeneralRemoteMethod {

	private InteropHandler handler;

	// Constructor of the service class
	public GeneralRemoteMethod(InteropHandler interopHandler) {
		// Hold the handler
		handler = interopHandler;

		// Log instantiation of an object of this class
		Debug.log("GeneralRemoteMethod instantiated with interop handler "
				+ handler.getClass().getName());
	}

	// Generalized method call method
	@WebMethod
	public String invokeRemoteMethod(int requestId, String methodName,
			String methodParams) {
		// Log call of this method
		Debug.log("CallRemoteMethod method called. Invoking " + methodName
				+ " for Request ID " + requestId);
		String result = "";
		
		// Make sure the handler exposes this method
		try {
			InteropTransferObject params = InteropTransferObject.deserialize(methodParams);
			
			// Log the parameters
			Debug.log("Dumping parameters for Request ID " + requestId + "\n" + params.dump() + "\n");
			
			InteropTransferObject to = (InteropTransferObject) (handler.getClass().getMethod(
					methodName, InteropTransferObject.class).invoke(handler,
							InteropTransferObject.deserialize(methodParams)));
			
			// Log the results
			Debug.log("Dumping results for Request ID " + requestId + "\n" + to.dump() + "\n");

			// Serialize the object
			result = to.serialize();
			
		} catch (Exception ex) {
			// Put the error message in there!
			Debug.logError(ex);
			InteropTransferObject to = new InteropTransferObject();
			try {
				to.add("ERROR", ex.getMessage());
			} catch (Exception e) {
			}
			result = to.serialize();
		}

		return result;
	}
}