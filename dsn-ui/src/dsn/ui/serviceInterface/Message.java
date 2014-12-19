package dsn.ui.serviceInterface;

import java.util.Calendar;

import dsn.commons.Utils.ServiceProvider;

public class Message {
	private ServiceProvider serviceProvider;
	private String message;
	private String id;
	private Calendar timestamp;
	
	public Message() {}
	
	public Message(ServiceProvider serviceProvider, String message, String id,
			Calendar timestamp) {
		this.serviceProvider = serviceProvider;
		this.message = message;
		this.id = id;
		this.timestamp = timestamp;
	}
	
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Calendar getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
}
