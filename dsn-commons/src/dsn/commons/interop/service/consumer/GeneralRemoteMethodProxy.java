package dsn.commons.interop.service.consumer;

public class GeneralRemoteMethodProxy implements
		dsn.commons.interop.service.consumer.GeneralRemoteMethod {
	private String _endpoint = null;
	private dsn.commons.interop.service.consumer.GeneralRemoteMethod generalRemoteMethod = null;

	public GeneralRemoteMethodProxy() {
		_initGeneralRemoteMethodProxy();
	}

	public GeneralRemoteMethodProxy(String endpoint) {
		_endpoint = endpoint;
		_initGeneralRemoteMethodProxy();
	}

	private void _initGeneralRemoteMethodProxy() {
		try {
			generalRemoteMethod = (new dsn.commons.interop.service.consumer.GeneralRemoteMethodServiceLocator())
					.getGeneralRemoteMethodPort();
			if (generalRemoteMethod != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) generalRemoteMethod)
							._setProperty(
									"javax.xml.rpc.service.endpoint.address",
									_endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) generalRemoteMethod)
							._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (generalRemoteMethod != null)
			((javax.xml.rpc.Stub) generalRemoteMethod)._setProperty(
					"javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public dsn.commons.interop.service.consumer.GeneralRemoteMethod getGeneralRemoteMethod() {
		if (generalRemoteMethod == null)
			_initGeneralRemoteMethodProxy();
		return generalRemoteMethod;
	}

	@Override
	public java.lang.String invokeRemoteMethod(int arg0, java.lang.String arg1,
			java.lang.String arg2) throws java.rmi.RemoteException {
		if (generalRemoteMethod == null)
			_initGeneralRemoteMethodProxy();
		return generalRemoteMethod.invokeRemoteMethod(arg0, arg1, arg2);
	}

}