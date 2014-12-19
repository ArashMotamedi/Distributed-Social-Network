package dsn.datamining;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class DsnAnalyzer extends StandardAnalyzer {
	
	public DsnAnalyzer(Version matchVersion, File stopwords) throws IOException {
		super(matchVersion, stopwords);
	}
	
//	@Override
//	public TokenStream tokenStream(String fieldName, Reader reader) {
//		Tokenizer tokenizer = new StandardTokenizer(Version.LUCENE_30, reader);
//		return new PorterStemFilter(tokenizer);
//	}

}
