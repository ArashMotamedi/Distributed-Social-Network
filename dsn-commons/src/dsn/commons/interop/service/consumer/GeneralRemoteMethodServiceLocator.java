/**
 * GeneralRemoteMethodServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dsn.commons.interop.service.consumer;

public class GeneralRemoteMethodServiceLocator extends
		org.apache.axis.client.Service implements
		dsn.commons.interop.service.consumer.GeneralRemoteMethodService {

	public GeneralRemoteMethodServiceLocator() {
	}

	public GeneralRemoteMethodServiceLocator(
			org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public GeneralRemoteMethodServiceLocator(java.lang.String wsdlLoc,
			javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for GeneralRemoteMethodPort
	private java.lang.String GeneralRemoteMethodPort_address = "http://localhost:8888/dsn-interop";

	@Override
	public java.lang.String getGeneralRemoteMethodPortAddress() {
		return GeneralRemoteMethodPort_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String GeneralRemoteMethodPortWSDDServiceName = "GeneralRemoteMethodPort";

	public java.lang.String getGeneralRemoteMethodPortWSDDServiceName() {
		return GeneralRemoteMethodPortWSDDServiceName;
	}

	public void setGeneralRemoteMethodPortWSDDServiceName(java.lang.String name) {
		GeneralRemoteMethodPortWSDDServiceName = name;
	}

	@Override
	public dsn.commons.interop.service.consumer.GeneralRemoteMethod getGeneralRemoteMethodPort()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(GeneralRemoteMethodPort_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getGeneralRemoteMethodPort(endpoint);
	}

	@Override
	public dsn.commons.interop.service.consumer.GeneralRemoteMethod getGeneralRemoteMethodPort(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			dsn.commons.interop.service.consumer.GeneralRemoteMethodPortBindingStub _stub = new dsn.commons.interop.service.consumer.GeneralRemoteMethodPortBindingStub(
					portAddress, this);
			_stub.setPortName(getGeneralRemoteMethodPortWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setGeneralRemoteMethodPortEndpointAddress(
			java.lang.String address) {
		GeneralRemoteMethodPort_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@Override
	public java.rmi.Remote getPort(Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		try {
			if (dsn.commons.interop.service.consumer.GeneralRemoteMethod.class
					.isAssignableFrom(serviceEndpointInterface)) {
				dsn.commons.interop.service.consumer.GeneralRemoteMethodPortBindingStub _stub = new dsn.commons.interop.service.consumer.GeneralRemoteMethodPortBindingStub(
						new java.net.URL(GeneralRemoteMethodPort_address), this);
				_stub.setPortName(getGeneralRemoteMethodPortWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException(
				"There is no stub implementation for the interface:  "
						+ (serviceEndpointInterface == null ? "null"
								: serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	@Override
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
			Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("GeneralRemoteMethodPort".equals(inputPortName)) {
			return getGeneralRemoteMethodPort();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	@Override
	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName(
				"http://provider.service.interop.commons.dsn/",
				"GeneralRemoteMethodService");
	}

	private java.util.HashSet ports = null;

	@Override
	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName(
					"http://provider.service.interop.commons.dsn/",
					"GeneralRemoteMethodPort"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("GeneralRemoteMethodPort".equals(portName)) {
			setGeneralRemoteMethodPortEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(
					" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
