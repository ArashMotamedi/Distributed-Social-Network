//package dsn.datamining.demo;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//import java.util.List;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.analysis.tokenattributes.TermAttribute;
//import org.apache.lucene.util.Version;
//
//public class TweetsToTagsParser {
//
//	/**
//	 * @param args
//	 * @throws IOException
//	 */
//	public static void main(String[] args) throws IOException {
//
////		Cloud cloud = new Cloud();
//		// cloud.setMaxTagsToDisplay(10);
//
//		long startTime = System.currentTimeMillis();
//
//		BufferedReader reader = null;
//		BufferedWriter writer = null;
//
//		try {
//			reader = new BufferedReader(new FileReader(
//					"data/twitter/tweets-short-processed.data"));
//			writer = new BufferedWriter(new FileWriter(
//					"data/twitter/tweets-short-tags.data"));
//
//			String line = null;
//			String[] tokens = null;
//			int counter = 0;
//
//			System.out.println("Processing:");
//
//			while ((line = reader.readLine()) != null) {
//				counter++;
//				tokens = line.split("\t\t:\t");
//				if (tokens.length != 2) // skip corner cases
//					continue;
//				// DEBUG: System.out.println(tokens[0] + " : " + tokens[1]);
//				String userId = tokens[0];
//				String userText = tokens[1];
//
//				// cloud.addText(userText);
//				process(userText, cloud);
//				List<Tag> tags = cloud.tags(new Tag.ScoreComparatorDesc());
//
//				// display
//				// System.out.print(tokens[0] + "\t:\t");
//				// for (Tag tag : tags) {
//				// System.out.print(tag.getScoreInt() + "|" + tag.getName() + " ");
//				// }
//				// System.out.println();
//
//				// write to a file
//				writer.write(userId + "\t\t:\t");
////				for (Tag tag : tags) {
//					writer.write(tag.getName() + "~" + tag.getScoreInt() + "|");
//				}
//				writer.write("\n");
//
////				cloud.clear();
//
//				if ((counter % 1000) == 0)
//					System.out.println(counter + "...");
//			}
//
//			System.out.println("done");
//
//			System.out.println("Users processed: " + counter);
//
//			long stopTime = System.currentTimeMillis();
//			long totalTime = stopTime - startTime;
//			System.out.println("Time (ms): " + (totalTime));
//
//		} finally {
//			if (reader != null)
//				reader.close();
//			if (writer != null)
//				writer.close();
//		}
//
//	}
//
//	private static void process(String string, Cloud cloud) throws IOException {
//
//		Reader reader = new StringReader(string);
//		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30, new File(
//				"data/twitter/stopwords.data"));
//		TokenStream tokenStream = analyzer.tokenStream("tag", reader);
//		TermAttribute term = tokenStream.addAttribute(TermAttribute.class);
//		while (tokenStream.incrementToken()) {
//			cloud.addTag(term.term());
//			// DEBUG System.out.print("[" + term.term() + "] ");
//		}
//	}
//
//}
