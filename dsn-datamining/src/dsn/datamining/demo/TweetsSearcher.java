package dsn.datamining.demo;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.Iterator;
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
import org.apache.lucene.index.TermVectorMapper;
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

public class TweetsSearcher {

	private static final String STOP_WORDS = "data/stopwords.data";
	private static final int FREQUENCY_THRESHOLD = 1000;

	private String indexPath;
	private Analyzer analyzer;

	public TweetsSearcher(String indexPath) throws IOException {
		this.indexPath = indexPath;
		this.analyzer = new StandardAnalyzer(Version.LUCENE_30, new File(
				STOP_WORDS));
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {

		String indexPath = "data/demo/index";
		String searchQuery = "what the bell";
		String userName = "rachelphillips";

		TweetsSearcher tweetsSearcher = new TweetsSearcher(indexPath);

		// tweetsSearcher.search(searchQuery, 10);
		// tweetsSearcher.getSimilarUsers(userName, 10);
		// tweetsSearcher.getTopWords(100);
		tweetsSearcher.getTopWordsForUser(userName, 10);

	}

	public void getTopWords(int count) throws IOException {
		
		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only
		IndexReader indexReader = indexSearcher.getIndexReader();

		Queue<TermWithFreq> freqQueue = new PriorityQueue<TermWithFreq>();
		
		TermEnum terms = indexReader.terms();
		while (terms.next() && count > 0) {
			int termFreq = terms.docFreq();
			if (termFreq < FREQUENCY_THRESHOLD)	// skip rare occurrences
				continue;
			String termText = terms.term().text();
			TermWithFreq termWithFreq = new TermWithFreq(termText, termFreq);
			freqQueue.add(termWithFreq);
		}
		
		System.out.println(freqQueue.size());
		
		for (int i = 0; i < count; i++) {
			System.out.println(freqQueue.poll());
		}
	}
	
	public void getTopWordsForUser(String userName, int count) throws IOException {
		
		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only
		IndexReader indexReader = indexSearcher.getIndexReader();
		
		FieldSortedTermVectorMapper vectorMapper = new FieldSortedTermVectorMapper(new TermVectorEntryFreqSortedComparator());

		int userDocId = getDocIdForUser(indexSearcher, userName);		
		indexReader.getTermFreqVector(userDocId, "tweets", vectorMapper);
		Map<String, SortedSet<TermVectorEntry>> vectorsMap =  vectorMapper.getFieldToTerms();
		SortedSet<TermVectorEntry> sortedTerms = vectorsMap.get("tweets");
		for (Iterator<TermVectorEntry> iter = sortedTerms.iterator(); iter.hasNext() && count > 0; count--) {
			TermVectorEntry vectorEntry = iter.next();
			System.out.println(vectorEntry.getTerm() + "/" + vectorEntry.getFrequency());
		}

	}

	public void getSimilarUsers(String userName, int count) throws IOException {

		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only

		int userDocId = getDocIdForUser(indexSearcher, userName);

		BooleanQuery booleanQuery = getSimilarUsersQuery(indexSearcher,
				userDocId);

		// there is a small bug in a form that search returns the user itself as
		// the number one hit. To deal with that we simply increase the search count
		// by 1 and discard the top search result later on.
		search(indexSearcher, booleanQuery, count + 1);

		indexSearcher.close();
	}

	public void searchString(String queryString, int count) throws IOException,
			ParseException {

		Directory dir = FSDirectory.open(new File(indexPath)); // Open Index
		IndexSearcher indexSearcher = new IndexSearcher(dir, true); // read-only

		QueryParser parser = new QueryParser(Version.LUCENE_30, "tweets",
				analyzer);
		Query query = parser.parse(queryString);

		search(indexSearcher, query, count);

		indexSearcher.close();
	}

	// performs the actual search
	private void search(IndexSearcher indexSearcher, Query query, int count)
			throws IOException {

		long startTime = System.currentTimeMillis();

		// Search
		TopDocs hits = indexSearcher.search(query, count);
		System.out.println(hits.totalHits + " hits.");

		// Setup snippets extractor/highlighter
		FragListBuilder fragListBuilder = new SimpleFragListBuilder();
		FragmentsBuilder fragmentsBuilder = new WhitespaceFragmentsBuilder(
				new String[] { "[" }, new String[] { "]" }, 5);
		FastVectorHighlighter highlighter = new FastVectorHighlighter(true,
				true, fragListBuilder, fragmentsBuilder);
		FieldQuery fieldQuery = highlighter.getFieldQuery(query);

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = indexSearcher.doc(scoreDoc.doc);
			System.out.println(doc.get("userId") + " [" + scoreDoc.score
					+ "] :");
			String[] snippets = highlighter.getBestFragments(fieldQuery,
					indexSearcher.getIndexReader(), scoreDoc.doc, "tweets", 50,
					10);
			for (String snippet : snippets)
				System.out.println("\t .." + snippet + ".. ");
			System.out.println();
			// DEBUG: System.out.println(" : " + doc.get("tweets"));
		}

		long stopTime = System.currentTimeMillis();
		long totalTime = stopTime - startTime;
		System.out.println("Total Time (ms): " + (totalTime));
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
}
