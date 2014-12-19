package dsn.datamining.demo;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

public class TweetsAnalyzer extends StandardAnalyzer {

	public TweetsAnalyzer(Version matchVersion, File stopwords)
			throws IOException {
		super(matchVersion, stopwords);
	}

}
