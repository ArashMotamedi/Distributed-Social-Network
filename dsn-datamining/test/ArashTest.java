import dsn.commons.interop.service.InteropTransferObject;
import dsn.datamining.RecommenderHandler;


public class ArashTest {

	public static RecommenderHandler rh = new RecommenderHandler();

	public static void main (String[] args) throws Exception
	{
		//getTopKeywords();
		search("pizza", "10");
	}
	
	public static void getTopKeywords() throws Exception
	{
		InteropTransferObject params = new InteropTransferObject();
		params.add("count", "100");
		params.add("offset", "0");
		
		System.out.println(rh.getTopWords(params).dump());
	}
	
	public static void search(String queryString, String topHits) throws Exception
	{
		InteropTransferObject params = new InteropTransferObject();
		params.add("queryString", queryString);
		params.add("topHits", topHits);
		
		System.out.println( rh.search(params).dump());
	}
}
