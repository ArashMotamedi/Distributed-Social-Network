package dsn.datamining.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TweetsIndexer {

	private static final String STOP_WORDS = "data/stopwords.data";

	private String indexPath;
	private String dataPath;
	private Analyzer analyzer;

	public TweetsIndexer(String indexPath, String dataPath) throws IOException {
		this.indexPath = indexPath;
		this.dataPath = dataPath;
		this.analyzer = new TweetsAnalyzer(Version.LUCENE_30, new File(STOP_WORDS));
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		//String dataPath = "data/demo/tweets-short-processed.data";
		String dataPath = "data/demo/dsn.data";
		String indexPath = "data/demo/index";

		TweetsIndexer tweetsIndexer = new TweetsIndexer(indexPath, dataPath);
		tweetsIndexer.index();
	}

	public void index() throws IOException {
		Directory indexDirectory = null;
		IndexWriter indexWriter = null;
		BufferedReader dataReader = null;

		try {
			indexDirectory = FSDirectory.open(new File(indexPath));
			dataReader = new BufferedReader(new FileReader(dataPath));

			// true - create new index, false - append
			indexWriter = new IndexWriter(indexDirectory, analyzer, true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			// indexWriter.setUseCompoundFile(false);

			System.out.println("Indexing..");
			int counter = 0;
			long startTime = System.currentTimeMillis();

			Document tweetDoc = null;
			while ((tweetDoc = this.getTweetDoc(dataReader)) != null) {
				indexWriter.addDocument(tweetDoc);
				counter++;
				if ((counter % 1000) == 0)
					System.out.println(counter + "...");
			}

			System.out.println("done\nOptimizig..");
			indexWriter.optimize();
			System.out.println("done");
			System.out.println("Indexed Documents: " + counter);

			long stopTime = System.currentTimeMillis();
			long totalTime = stopTime - startTime;
			System.out.println("Total Time (ms): " + (totalTime));

		} finally {
			indexWriter.close();
			dataReader.close();
		}
	}

	private Document getTweetDoc(BufferedReader dataReader) throws IOException {
		Document tweetDoc = null;
		String line = null;
		String[] tokens = null;

		if ((line = dataReader.readLine()) != null) {
			tokens = line.split("\t:\t");
			// if (tokens.length != 2) // skip corner cases
			// continue;
			// DEBUG: System.out.println(tokens[0] + " : " + tokens[2]);
			tweetDoc = new Document();
			tweetDoc.add(new Field("userId", tokens[0], Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			tweetDoc.add(new Field("tweets", tokens[1], Field.Store.YES,
					Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
		}
		return tweetDoc;
	}
}
