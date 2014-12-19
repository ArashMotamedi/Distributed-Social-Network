package dsn.scheduler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateIndexHelper extends CalendarHelper{
	public String index_check = null;
	public String indexFile = null;
	
	public UpdateIndexHelper () throws IOException {
		indexFile = new java.io.File(".").getCanonicalPath() + "/indexDir/latest_update_index.txt";
	}
	
	public String lastIndexRun() throws IOException {		
		File file;
	    BufferedReader reader;
	    String line = null;
	    String urlList = null;
	    
		try {
			file = new File(indexFile);
			//System.out.println("\nindexFile: " + file);
			reader = new BufferedReader(new FileReader(file));

			while((line = reader.readLine()) != null) {
				urlList = line;
				System.out.println(urlList);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return urlList;  
	}
	
	public void newIndexRun() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datestamp = new Date();
        
		try {
			FileWriter fs = new FileWriter(indexFile);
			BufferedWriter out = new BufferedWriter(fs);

			out.write(formatDateTime(datestamp));
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
