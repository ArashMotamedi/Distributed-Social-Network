package dsn.datamining.demo;

import dsn.commons.interop.service.provider.InteropProvider;

public class TweetsMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		InteropProvider interopProvider = new InteropProvider(
				"http://192.168.1.107:8080/dsn-interop", new TweetsHandler());
	}

}
