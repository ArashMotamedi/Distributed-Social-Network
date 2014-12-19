package dsn.bots;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import dsn.commons.development.Debug;
import dsn.scheduler.DBInfo;

public class GeneralBot {

	String spName = null;
	URL url = null;
	private static final int BUFFER_SIZE = 8192;

	public GeneralBot() {

	}

	public void saveMessagesToDB(String[] user_id, String[] sp_id, String[] message, String[] timestamp, String[] sm_sp_id) {
		DBInfo dbInfo = new DBInfo();
		 
		for(int i = 0; i < user_id.length; i++) {
			try {
				dbInfo.insertNewStatus(user_id[i], sp_id[i], message[i], timestamp[i], sm_sp_id[i]);				
			} catch (Exception e) {
				Debug.logError(e);
			}
			
		}
		
	}
	
	public void saveToFileXML(String urlString, String fileName)
			throws IOException {
		url = new URL(urlString);
		File file = new File(fileName);

		System.out.println("save to file: " + file);

		URLConnection conn = url.openConnection();

		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(file);

		transfer(is, os);

		is.close();
		os.close();
	}

	public void saveToFileJSON(String urlString, String fileName)
			throws IOException {
		url = new URL(urlString);
		File file = new File(fileName);

		System.out.println("save to file: " + file);

		URLConnection conn = url.openConnection();

		InputStream is = conn.getInputStream();
		OutputStream os = new FileOutputStream(file);

		transfer(is, os);

		is.close();
		os.close();
	}

	public void printResultsXML(String urlString) throws IOException,
			ParserConfigurationException, SAXException, TransformerException {

		url = new URL(urlString);

		URLConnection conn = url.openConnection();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(conn.getInputStream());

		TransformerFactory factory2 = TransformerFactory.newInstance();
		Transformer xform = factory2.newTransformer();

		xform.transform(new DOMSource(doc), new StreamResult(System.out));
	}

	public void PrintResultsJSON(String urlString) throws IOException,
			ParserConfigurationException, SAXException, TransformerException {
		
		String s;
		url = new URL(urlString);
		URLConnection conn = url.openConnection();
		
		InputStream is;
		BufferedReader dis;
		
		is = conn.getInputStream();
		
		dis = new BufferedReader(new InputStreamReader(is));

		while ((s = dis.readLine()) != null) {
			System.out.println(s);
         }
		
		is.close();
		dis.close();
	}

	private void transfer(InputStream is, OutputStream os) throws IOException {
		byte buffer[] = new byte[BUFFER_SIZE];
		int size = 0;

		do {
			size = is.read(buffer);
			if (size != -1)
				os.write(buffer, 0, size);
		} while (size != -1);
		is.close();
		os.close();
	}
	
	public String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    
	    f.close();
	    return new String(buffer);
	}
}
