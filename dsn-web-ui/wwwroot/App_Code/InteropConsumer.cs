using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for InteropConsumer
/// </summary>
public class InteropConsumer
{
	private string endpoint = "";
	private static int requestId = 0;

	public InteropConsumer(string providerEndpoint)
	{
		endpoint = providerEndpoint;
	}

	public InteropTransferObject consumeService(String methodName, InteropTransferObject methodParams)
	{
		// Increment requestID
		requestId += 1;

		// Prepare a result object
		InteropTransferObject result = null;

		try
		{
			dsnInterop.GeneralRemoteMethodService service = new dsnInterop.GeneralRemoteMethodService();
			service.Url = endpoint;
			result = InteropTransferObject.deserialize(service.invokeRemoteMethod(requestId, methodName, methodParams.serialize()));


			string errorMessage = null;
			try
			{
				errorMessage = result.getValue("ERROR");
			}
			catch {}

			if (!string.IsNullOrEmpty(errorMessage))
				throw new Exception(errorMessage);

		}
		catch
		{
			throw;
		}

		// Return the result
		return result;
	}
}