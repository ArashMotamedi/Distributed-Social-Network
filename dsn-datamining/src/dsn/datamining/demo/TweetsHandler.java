package dsn.datamining.demo;

import java.util.ArrayList;

import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.InteropHandler;

public class TweetsHandler extends InteropHandler {

	public InteropTransferObject helloWorld(InteropTransferObject params)
			throws Exception {
		InteropTransferObject to = new InteropTransferObject();

		to.add("message", "hello from me");

		return to;
	}

	public InteropTransferObject getSimilarUsers(InteropTransferObject params)
			throws Exception {
		InteropTransferObject to = new InteropTransferObject();
		
		ArrayList<String> similarUsers = null;

		String userName = params.getValue("userId");
		Integer count = Integer.parseInt(params.getValue("count"));
		to.add("message", "Top " + count + " similar users for " + userName);
		
		TweetsSearcher tweetsSearcher = new TweetsSearcher("data/twitter/index");
//		similarUsers = tweetsSearcher.getSimilarUsers(userName, count);
//		
//		to.add("similarUsers", similarUsers.toArray(new String[similarUsers.size()]));

		return to;
	}

}
