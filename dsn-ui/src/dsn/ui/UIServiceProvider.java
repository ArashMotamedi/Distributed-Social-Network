package dsn.ui;

import dsn.commons.interop.service.provider.InteropProvider;

public class UIServiceProvider {
	public static void main(String[] args) throws Exception {
		
		InteropProvider ip = new InteropProvider(
				"http://Arash-VAIO:8889/dsn-ui", new UIServiceHandler());
				
	}
}
