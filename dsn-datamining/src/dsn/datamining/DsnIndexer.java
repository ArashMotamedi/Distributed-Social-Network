package dsn.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class DsnIndexer {

	private static final String INDEX_PATH = "data/index";
	private static final String STOP_WORDS = "data/stopwords.data";

	private Analyzer analyzer;
	private String indexPath;
	private boolean newIndex;

	public DsnIndexer() throws IOException {
		this(INDEX_PATH, new DsnAnalyzer(Version.LUCENE_30,
				new File(STOP_WORDS)), false);
	}

	public DsnIndexer(boolean newIndex) throws IOException {
		this(INDEX_PATH, new DsnAnalyzer(Version.LUCENE_30,
				new File(STOP_WORDS)), newIndex);
	}

	public DsnIndexer(String indexPath, Analyzer analyzer, boolean newIndex) throws IOException {
		this.indexPath = indexPath;
		this.analyzer = analyzer;
		this.newIndex = newIndex;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void addToIndex(Map<String, String> updates) throws IOException {
		
		Directory indexDirectory = FSDirectory.open(new File(indexPath));
		IndexWriter indexWriter = new IndexWriter(indexDirectory, analyzer, newIndex,
				IndexWriter.MaxFieldLength.UNLIMITED);;
//			long startTime = System.currentTimeMillis();

			// System.out.print("Indexing..");
			int counter = 0;

			Document doc = null;
			for (String userId : updates.keySet()) {
				doc = this.getDoc(indexDirectory, userId, updates.get(userId));
				indexWriter.updateDocument(
						new Term("userId", doc.get("userId")), doc);
				counter++;
				if ((counter % 1000) == 0)
					System.out.println(counter + "...");
			}

			indexWriter.close();
			// System.out.print("done\nOptimizig..");
			//indexWriter.optimize();
			//System.out.println("done\nIndexed Documents: " + counter);

//			long stopTime = System.currentTimeMillis();
//			long totalTime = stopTime - startTime;
//			System.out.println("Total Time (ms): " + (totalTime));
	}

	private Document getDoc(Directory indexDirectory, String userId,
			String updates) throws IOException {
		IndexSearcher indexSearcher = new IndexSearcher(indexDirectory, true);
		Document doc = null;

		Query query = new TermQuery(new Term("userId", userId));
		TopDocs hits = indexSearcher.search(query, 1);
//		System.out.println("Found " + hits.totalHits + // Stats
//				" users with the name '" + userId + "'");
		if (hits.totalHits == 0) {
			doc = new Document();
			doc.add(new Field("userId", userId, Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
		} else {
			int docId = hits.scoreDocs[0].doc;
			doc = indexSearcher.doc(docId);
		}

		doc.add(new Field("updates", updates, Field.Store.YES,
				Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

		indexSearcher.close();

		return doc;
	}

//	public static void main(String[] args) throws IOException, InterruptedException {
//
//		// create an indexer with a new index
//		DsnIndexer dsnIndexer = new DsnIndexer(true);
//		Map<String, String> updatesPerUser = new HashMap<String, String>();
//
//		BufferedReader dataReader = null;
//		try {
//			dataReader = new BufferedReader(new FileReader(
//					"data/demo/tweets-5000.data"));
//
//			String line = null;
//			String[] tokens = null;
//			int counter = 0;
//
//			long startTime = System.currentTimeMillis();
//			
//			while ((line = dataReader.readLine()) != null) {
//				tokens = line.split("\t");
//				if (tokens.length != 3) // skip corner cases
//					continue;
//				// DEBUG: System.out.println(tokens[0] + " : " + tokens[2]);
//				if (updatesPerUser.containsKey(tokens[0])) {
//					// System.out.println(counter + " updates read");
//					dsnIndexer.addToIndex(updatesPerUser);
//					updatesPerUser.clear();
//				} else {
//					updatesPerUser.put(tokens[0], tokens[2]);
//				}
//				counter++;
//				if ((counter % 1000) == 0)
//					System.out.println(counter + " records..");
//			}
//
//			System.out.println("Done\nTotal updates: " + counter);
//			long stopTime = System.currentTimeMillis();
//			long totalTime = stopTime - startTime;
//			System.out.println("Total Time (ms): " + (totalTime));
//
//		} finally {
//			dataReader.close();
//		}
//	}

}
