package dsn.datamining.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TweetsParser {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		long startTime = System.currentTimeMillis();
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		
		try {
			reader = new BufferedReader(new FileReader("data/demo/tweets-500k.data"));
			writer = new BufferedWriter(new FileWriter("data/demo/tweets-500k-processed.data"));
			
			String line = null;
			String[] tokens = null;
			int counter = 0;
			
			Map<String, String> tweetsByUser = new HashMap<String, String>(); 
			
			System.out.println("Parsing:");
			
			while ((line = reader.readLine()) != null) {
				counter++;
				tokens = line.split("\t");
				if (tokens.length != 3)		// skip corner cases
					continue;
				//DEBUG: System.out.println(tokens[0] + " : " + tokens[2]);
				if (tweetsByUser.containsKey(tokens[0])) {
					String tweet = tweetsByUser.get(tokens[0]);
					tweet += (" " + tokens[2]);
					tweetsByUser.put(tokens[0], tweet);
				} else {
					tweetsByUser.put(tokens[0], tokens[2]);
				}
				if ((counter % 100000) == 0)
					System.out.println(counter + "...");
			}
			
			System.out.println("done");
			
			System.out.println("Total users: " + tweetsByUser.size());
			System.out.println("Total tweets: " + counter);
			for (String user : tweetsByUser.keySet())
				writer.write(user + "\t\t:\t" + tweetsByUser.get(user) + "\n");
			
			long stopTime = System.currentTimeMillis();
			long totalTime = stopTime - startTime;
			System.out.println("Time (ms): " + (totalTime));
			
		} finally {
			if (reader != null)
				reader.close();
			if (writer != null)
				writer.close();
		}
	}
}
