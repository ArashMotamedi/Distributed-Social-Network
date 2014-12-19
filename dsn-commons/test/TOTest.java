import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;


public class TOTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		InteropTransferObject to = new InteropTransferObject();
		String[] values = new String[10];
		
		to.add("test", values);
		
		Debug.log(to.dump());
		Debug.log(to.serialize());
	}

}
