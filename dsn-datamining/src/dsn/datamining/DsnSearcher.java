package dsn.datamining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.FieldSortedTermVectorMapper;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermVectorEntry;
import org.apache.lucene.index.TermVectorEntryFreqSortedComparator;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.search.vectorhighlight.WhitespaceFragmentsBuilder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import dsn.datamining.demo.TermWithFreq;

public class DsnSearcher {

	private static final String INDEX_PATH = "data/demo/index";
	private static final String STOP_WORDS = "data/stopwords.data";
	private static final int FREQUENCY_THRESHOLD = 100;

	private String indexPath;
	private Analyzer analyzer;

	public DsnSearcher() throws IOException {
		this(INDEX_PATH);
	}

	public DsnSearcher(String indexPath) throws IOException {
		this.indexPath = indexPath;
		this.analyzer = new StandardAnalyzer(Version.LUCENE_30, new File(
				STOP_WORDS));
	}

	public List<String> getTopWords(int count, int offset) throws IOException {
		
		List<String> result = new ArrayList<String>();

		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only
		IndexReader indexReader = indexSearcher.getIndexReader();

		Queue<TermWithFreq> freqQueue = new PriorityQueue<TermWithFreq>();

		TermEnum terms = indexReader.terms();
		while (terms.next() && count > 0) {
			int termFreq = terms.docFreq();
			if (termFreq < FREQUENCY_THRESHOLD) // skip rare occurrences
				continue;
			String termText = terms.term().text();
			TermWithFreq termWithFreq = new TermWithFreq(termText, termFreq);
			freqQueue.add(termWithFreq);
		}

		System.out.println(freqQueue.size());
		for (int i = 0; i < offset; i++) {
			freqQueue.poll();
		}

		for (int i = 0; i < count; i++) {
			TermWithFreq twf = freqQueue.poll();
			result.add(twf.getTermText() + "/" + twf.getTermFreq());
		}
		
		return result;
	}

	public List<String> getTopWordsForUser(String userName, int count, int offset)
			throws IOException {

		List<String> results = new ArrayList<String>();
		
		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only
		IndexReader indexReader = indexSearcher.getIndexReader();

		FieldSortedTermVectorMapper vectorMapper = new FieldSortedTermVectorMapper(
				new TermVectorEntryFreqSortedComparator());

		int userDocId = getDocIdForUser(indexSearcher, userName);
		indexReader.getTermFreqVector(userDocId, "tweets", vectorMapper);
		Map<String, SortedSet<TermVectorEntry>> vectorsMap = vectorMapper
				.getFieldToTerms();
		SortedSet<TermVectorEntry> sortedTerms = vectorsMap.get("tweets");
		for (Iterator<TermVectorEntry> iter = sortedTerms.iterator(); iter
				.hasNext() && count > 0; count--) {
			TermVectorEntry vectorEntry = iter.next();
			String result = vectorEntry.getTerm() + "/"
			+ vectorEntry.getFrequency();
			// System.out.println(result);
			results.add(result);
		}
		
		return results;
	}

	public List<String> getSimilarUsers(String userName, int count) throws IOException {

		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only

		int userDocId = getDocIdForUser(indexSearcher, userName);
		BooleanQuery booleanQuery = getSimilarUsersQuery(indexSearcher,
				userDocId);

		// there is a small bug in a form that search returns the user itself as
		// the number one hit. To deal with that we simply increase the search
		// count
		// by 1 and discard the top search result later on.
		List<String> similarUsers = search(indexSearcher, booleanQuery, count + 1);
		indexSearcher.close();
		
		return similarUsers;
	}

	public List<String> searchString(String queryString, int count) throws IOException,
			ParseException {

		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only

		QueryParser parser = new QueryParser(Version.LUCENE_30, "tweets",
				analyzer);
		Query query = parser.parse(queryString);

		List<String> searchResults = search(indexSearcher, query, count);

		indexSearcher.close();
		
		return searchResults;
	}

	// performs the actual search
	private List<String> search(IndexSearcher indexSearcher, Query query, int count)
			throws IOException {

		List<String> searchResults = new ArrayList<String>();
		
		long startTime = System.currentTimeMillis();

		// Search
		TopDocs hits = indexSearcher.search(query, count);
		System.out.println("Searching.. " + hits.totalHits + " hits.");

		// Setup snippets extractor/highlighter
		FragListBuilder fragListBuilder = new SimpleFragListBuilder();
		FragmentsBuilder fragmentsBuilder = new WhitespaceFragmentsBuilder(
				new String[] { "<b>" }, new String[] { "</b>" }, 7);
		FastVectorHighlighter highlighter = new FastVectorHighlighter(true,
				true, fragListBuilder, fragmentsBuilder);
		FieldQuery fieldQuery = highlighter.getFieldQuery(query);

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			StringBuilder sb = new StringBuilder();
			
			Document doc = indexSearcher.doc(scoreDoc.doc);
			sb.append(doc.get("userId") + " [" + scoreDoc.score + "] :\n");
//			System.out.println(doc.get("userId") + " [" + scoreDoc.score
//					+ "] :");
			String[] snippets = highlighter.getBestFragments(fieldQuery,
					indexSearcher.getIndexReader(), scoreDoc.doc, "tweets", 50,
					10);
			for (String snippet : snippets) {
				//System.out.println("\t .." + snippet + ".. ");
				sb.append(" " + snippet + "\n");
			}
			
			searchResults.add(sb.toString());
		}

		long stopTime = System.currentTimeMillis();
		long totalTime = stopTime - startTime;
		System.out.println("Total Time (ms): " + (totalTime));
		
		return searchResults;
	}

	// build a user-based search query
	private BooleanQuery getSimilarUsersQuery(IndexSearcher indexSearcher,
			int userDocId) throws IOException {

		BooleanQuery booleanQuery = new BooleanQuery();
		IndexReader indexReader = indexSearcher.getIndexReader();
		TermFreqVector termFreqVector = indexReader.getTermFreqVector(
				userDocId, "tweets");
		// DEBUG: System.out.println(termFreqVector);
		String[] terms = termFreqVector.getTerms();
		int[] termFreuencies = termFreqVector.getTermFrequencies();
		System.out.print("Common terms: ");
		for (int i = 0; i < terms.length; i++) {
			if (termFreuencies[i] > 2) { // skip low frequency occurrences
				System.out.print(terms[i] + "/" + termFreuencies[i] + " ");
				TermQuery termQuery = new TermQuery(
						new Term("tweets", terms[i]));
				termQuery.setBoost((float) termFreuencies[i]);
				booleanQuery.add(termQuery, BooleanClause.Occur.SHOULD);
			}
		}
		System.out.println();

		return booleanQuery;
	}

	// find userName associated doc in index
	private int getDocIdForUser(IndexSearcher indexSearcher, String userName)
			throws IOException {
		Query query = new TermQuery(new Term("userId", userName));
		TopDocs hits = indexSearcher.search(query, 1);
		System.out.println("Found " + hits.totalHits + // Stats
				" users with the name '" + userName + "'");
		return hits.scoreDocs[0].doc;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {

		String searchQuery = "what the bell";
		String userName = "rachelphillips";

		DsnSearcher searcher = new DsnSearcher();

		List<String> results = null; 
		
		// results = searcher.searchString(searchQuery, 10);		
		// results = searcher.getSimilarUsers(userName, 10);
		// results = searcher.getTopWords(100);
		results = searcher.getTopWordsForUser(userName, 10, 10);
		
		for (String searchResult : results)
			System.out.println(searchResult);

	}

}
