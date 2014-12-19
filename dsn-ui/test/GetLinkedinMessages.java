import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.crypto.NodeSetData;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dsn.commons.Utils;
import dsn.commons.Utils.ServiceProvider;
import dsn.commons.development.Debug;


public class GetLinkedinMessages {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws ParseException, XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub
		String resp = Utils.getUrl("http://api.linkedin.com/v1/people/~/network/updates?scope=self&type=SHAR",
				ServiceProvider.LINKEDIN, 
				"099c3ca8-b05c-4e0a-9828-66cbd92667f5",
				"450cbf9b-8cbe-4282-826f-b1662ee072d6");
		
		Debug.log(resp);
		
		Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(resp)));
		NodeList nlTimestamp = d.getElementsByTagName("timestamp");
		NodeList nlUpdateKey = d.getElementsByTagName("update-key");
		NodeList nlStatus = d.getElementsByTagName("current-status");
		
		String[][] messages = new String[3][nlTimestamp.getLength()];
		
		for (int i = 0; i < nlTimestamp.getLength(); i++)
		{
			messages[0][i] = nlUpdateKey.item(i).getFirstChild().getNodeValue();
			messages[1][i] = nlTimestamp.item(i).getFirstChild().getNodeValue();
			messages[2][i] = nlStatus.item(i).getFirstChild().getNodeValue();
		}
	}

}
