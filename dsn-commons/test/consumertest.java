import dsn.commons.development.Debug;
import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.consumer.InteropConsumer;

public class consumertest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InteropConsumer ic = new InteropConsumer(
				"http://localhost:8080/dsn-interop");
		try {
			Debug.log(ic.consumeService("salam", new InteropTransferObject())
					.serialize());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
