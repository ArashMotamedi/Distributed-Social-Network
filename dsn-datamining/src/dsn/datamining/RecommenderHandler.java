package dsn.datamining;

import java.util.List;

import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.InteropHandler;

public class RecommenderHandler extends InteropHandler {

	public InteropTransferObject helloWorld(InteropTransferObject params)
			throws Exception {

		System.out.println("RecommenderHandler#helloWorld.. ");
		System.out.println("hello from dsn-dm-recommender");

		// String message = params.getValue("message");
		// System.out.println(message);
		//
		InteropTransferObject to = new InteropTransferObject();
		to.add("message", "hello from dsn-dm-recommeder");

		return to;
	}

	public InteropTransferObject search(InteropTransferObject params)
			throws Exception {
		System.out.println("RecommenderHandler#search:");
		System.out.println("searching..");

		String queryString = params.getValue("queryString");
		int topHits = Integer.parseInt(params.getValue("topHits"));

		DsnSearcher searcher = new DsnSearcher();
		List<String> searchResultsList = searcher.searchString(queryString, topHits); 
		String[] searchResults = searchResultsList.toArray(new String[0]);

		InteropTransferObject to = new InteropTransferObject();
		to.add("searchResults", searchResults);
		to.add("message", "RecommenderHandler#search: done!");
		return to;
	}

	public InteropTransferObject getTopWords(InteropTransferObject params)
			throws Exception {
		System.out.println("RecommenderHandler#getTopWords:");
		System.out.println("getting top words..");

		int count = Integer.parseInt(params.getValue("count"));
		int offset = Integer.parseInt(params.getValue("offset"));

		DsnSearcher searcher = new DsnSearcher();
		List<String> topWordsList = searcher.getTopWords(count, offset); 
		String[] topWords = topWordsList.toArray(new String[0]);

		InteropTransferObject to = new InteropTransferObject();
		to.add("topWords", topWords);
		to.add("message", "RecommenderHandler#getTopWords: done!");
		return to;
	}

	public InteropTransferObject getTopWordsForUser(InteropTransferObject params)
			throws Exception {
		System.out.println("RecommenderHandler#getTopWordsForUser:");
		System.out.println("getting top words for user..");

		String userId = params.getValue("userId");
		int count = Integer.parseInt(params.getValue("count"));
		//int offset = Integer.parseInt(params.getValue("offset"));
		int offset = 10;

		DsnSearcher searcher = new DsnSearcher();
		
		List<String> topWordsList = searcher.getTopWordsForUser(userId, count, offset); 
		String[] topWords = topWordsList.toArray(new String[0]);

		InteropTransferObject to = new InteropTransferObject();
		to.add("topWords", topWords);
		to.add("message", "RecommenderHandler#getTopWords: done!");
		return to;
	}

	public InteropTransferObject getSimilarUsers(InteropTransferObject params)
			throws Exception {
		System.out.println("RecommenderHandler#getSimilarUsers:");
		System.out.println("getting similar users..");

		String userId = params.getValue("userId");
		int count = Integer.parseInt(params.getValue("count"));

		DsnSearcher searcher = new DsnSearcher();

		List<String> similarUsersList = searcher.getSimilarUsers(userId, count);
		String[]  similarUsers = similarUsersList.toArray(new String[0]);

		InteropTransferObject to = new InteropTransferObject();
		to.add("similarUsers", similarUsers);
		to.add("message", "RecommenderHandler#getSimilarUsers: done!");
		return to;
	}
}
