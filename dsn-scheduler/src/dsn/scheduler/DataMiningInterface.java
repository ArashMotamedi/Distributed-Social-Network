package dsn.scheduler;

import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;
import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.consumer.InteropConsumer;
import dsn.scheduler.DBInfo;

public class DataMiningInterface {
	
	public static DBInfo dbInfo;
	
	public static void main(String last_checked) throws Exception {
		
		InteropTransferObject to = UpdateIndex(last_checked);
		
		Debug.log(to.getValue("message"));
		
	}
			
	public static InteropTransferObject UpdateIndex(String last_checked) throws Exception {

		dbInfo = new DBInfo();
		ConfigSet cs = new ConfigSet(ConfigType.INTEROP);
		InteropConsumer ic = new InteropConsumer(cs.getAttribute("indexerendpoint"));
		
		UserStatusUpdates userStatusUpdates = new UserStatusUpdates();
		
		userStatusUpdates = dbInfo.getLatestMessages(last_checked);
		String[] users_i = userStatusUpdates.users;
		String[] messages_i = userStatusUpdates.messages;
		
		InteropTransferObject params = new InteropTransferObject();
		params.add("users", users_i);
		params.add("messages", messages_i);

		InteropTransferObject to = ic.consumeService("updateIndex", params);
		
		return to;
	}

}
