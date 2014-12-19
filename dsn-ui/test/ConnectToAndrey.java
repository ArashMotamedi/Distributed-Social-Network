import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.consumer.InteropConsumer;


public class ConnectToAndrey {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		InteropConsumer ic = new InteropConsumer("http://192.168.1.107:8080/dsn-dm-indexer");
		InteropTransferObject to = ic.consumeService("helloWorld", new InteropTransferObject());
		Debug.log(to.getValue("message"));
	}
}
