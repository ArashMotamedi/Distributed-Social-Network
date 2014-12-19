package dsn.datamining;

import dsn.commons.interop.service.provider.InteropProvider;

public class DataminingApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		InteropProvider indexerProvider = new InteropProvider(
				"http://Arash-VAIO:8888/dsn-dm-indexer", new IndexerHandler());
		
		InteropProvider recommenderProvider = new InteropProvider(
				"http://Arash-VAIO:8888/dsn-dm-recommender", new RecommenderHandler());

/*		
		InteropProvider indexerProvider = new InteropProvider(
				"http://10.185.18.207:8080/dsn-dm-indexer", new IndexerHandler());
		
		InteropProvider recommenderProvider = new InteropProvider(
				"http://10.185.18.207:8080/dsn-dm-recommender", new RecommenderHandler());
*/		
	}

}
