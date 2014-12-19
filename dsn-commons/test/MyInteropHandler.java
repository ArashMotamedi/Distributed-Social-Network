import dsn.commons.interop.service.InteropTransferObject;
import dsn.commons.interop.service.provider.InteropHandler;

public class MyInteropHandler extends InteropHandler {
	public InteropTransferObject salam(InteropTransferObject params)
			throws Exception {
		params.add("khodaee", "che hali mide!");
		return params;
	}
}