import java.util.Calendar;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;


public class TestDateFunction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fbDate = "2010-11-10T06:47:27+0000";
		String twDate = "Tue Nov 23 23:20:44 +0000 2010";
		String liDate = "1291013367333";
		
		Calendar c = Utils.getCalendar(ServiceProvider.LINKEDIN, liDate);
		Debug.log(Utils.getTimestamp(c));
				
	}

}
