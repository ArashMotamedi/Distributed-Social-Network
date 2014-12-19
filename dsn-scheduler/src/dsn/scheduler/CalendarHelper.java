package dsn.scheduler;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CalendarHelper extends GregorianCalendar {
	
	public DateFormat dateFormat;
	public Date datestamp;
	public Time timestamp;
    
    public Integer[] getCurrentDateTime() {
    	Calendar cal = new GregorianCalendar();
		
    	int year = cal.get((Calendar.YEAR));
		int month = cal.get((Calendar.MONTH));
		int day = cal.get((Calendar.DAY_OF_MONTH));
    	int hour24 = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		Integer currentDateTime[] = { year, month, day, hour24, min, sec };

		return currentDateTime;
		
    }
    
    public static String formatTimestamp(String timestamp) {    	
    	String[] thisdate = null;
    	
    	thisdate = timestamp.split("T");
    	String thistime = thisdate[1];
    	String[] time = thistime.split("\\+");
    	String formated_timestamp = thisdate[0]+ " " +time[0];
    	System.out.println(formated_timestamp);

    	return formated_timestamp;
    }
    public String formatDateTime(Date datestamp) {
    	dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        return dateFormat.format(datestamp);
    }
        
    public Integer formatGetYear(Date datestamp) {
    	dateFormat = new SimpleDateFormat("yyyy");
		return Integer.parseInt(dateFormat.format(datestamp));
    }

    public Integer formatGetMonth(Date datestamp) {
    	dateFormat = new SimpleDateFormat("MM");
        
		return Integer.parseInt(dateFormat.format(datestamp));
    }

    public Integer formatGetDay(Date datestamp) {
    	dateFormat = new SimpleDateFormat("dd");
        
		return Integer.parseInt(dateFormat.format(datestamp));
    }

    public Integer formatGetHour(Date timestamp) {
    	dateFormat = new SimpleDateFormat("HH");
        
		return Integer.parseInt(dateFormat.format(timestamp));
    }

    public Integer formatGetMin(Date timestamp) {
    	dateFormat = new SimpleDateFormat("mm");
        
		return Integer.parseInt(dateFormat.format(timestamp));
    }
    
    public Integer formatGetSec(Date timestamp) {
    	dateFormat = new SimpleDateFormat("ss");
        
		return Integer.parseInt(dateFormat.format(timestamp));
    }
}
