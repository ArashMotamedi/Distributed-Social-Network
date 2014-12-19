package dsn.datamining;

import java.util.HashMap;
import java.util.Map;

import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.InteropHandler;

public class IndexerHandler extends InteropHandler {

	public InteropTransferObject helloWorld(InteropTransferObject params)
			throws Exception {
		
		System.out.println("IndexerHandler#helloWorld:");
		System.out.println("hello from dsn-dm-indexer");
		
//		String message = params.getValue("message");
//		System.out.println(message);
//		
		InteropTransferObject to = new InteropTransferObject();
		to.add("message", "hello from dsn-dm-indexer");

		return to;
	}
	
	public InteropTransferObject updateIndex(InteropTransferObject params) throws Exception {
		System.out.println("IndexerHandler#updateIndex:");
		System.out.println("trying to update index..");
		
		Map<String, String> updates = new HashMap<String, String>();
		String[] userIds = params.getValues("users");
		String[] messages = params.getValues("messages");
		
		//String[] userIds = {"1", "2", "3", "4"};
		//String[] messages = {"hmm", "hem hem", "oh oh", "ha"};
		
		int length = userIds.length;
		for(int i = 0; i < length; i++) {
			updates.put(userIds[i], messages[i]);
		}
		
		DsnIndexer indexer = new DsnIndexer(true);
		indexer.addToIndex(updates);
		
		InteropTransferObject to = new InteropTransferObject();
		to.add("message", "IndexerHandler#updateIndex: done!");

		return to;
	}

}
