package dsn.datamining.demo;

public class TermWithFreq implements Comparable<TermWithFreq>{
	
	private final String termText;
	private final Integer termFreq;
	
	public TermWithFreq(String termText, int termFreq) {
		super();
		this.termText = termText;
		this.termFreq = termFreq;
	}

	public String getTermText() {
		return termText;
	}

	public Integer getTermFreq() {
		return termFreq;
	}

	@Override
	public int compareTo(TermWithFreq o) {
		return o.getTermFreq().compareTo(termFreq);
	}

	@Override
	public String toString() {
		return termText + "/" + termFreq;
	}
	
	
	
}
