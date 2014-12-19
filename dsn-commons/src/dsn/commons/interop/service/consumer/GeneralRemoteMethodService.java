/**
 * GeneralRemoteMethodService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dsn.commons.interop.service.consumer;

public interface GeneralRemoteMethodService extends javax.xml.rpc.Service {
	public java.lang.String getGeneralRemoteMethodPortAddress();

	public dsn.commons.interop.service.consumer.GeneralRemoteMethod getGeneralRemoteMethodPort()
			throws javax.xml.rpc.ServiceException;

	public dsn.commons.interop.service.consumer.GeneralRemoteMethod getGeneralRemoteMethodPort(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
