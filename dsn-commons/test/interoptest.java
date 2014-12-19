import dsn.commons.interop.service.provider.InteropProvider;

public class interoptest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InteropProvider ip = new InteropProvider(
				"http://localhost:8888/dsn-interop", new MyInteropHandler());
	}
}
