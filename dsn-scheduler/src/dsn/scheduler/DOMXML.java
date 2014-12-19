package dsn.scheduler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.w3c.dom.*;

import dsn.bots.facebook.FacebookBot;
import dsn.bots.flickr.FlickrBot;
import dsn.bots.linkedin.LinkedinBot;
import dsn.bots.twitter.TwitterBot;

import javax.xml.parsers.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class DOMXML {

	/**
	 * userAccounts[]: facebook, flickr, twitter; userAccounts[]:
	 * 0,0,"cldulay0504";
	 */

	public static FlickrBot flickrBot;
	public static TwitterBot twitterBot;
	public static FacebookBot facebookBot;
	public static LinkedinBot linkedinBot;
	public static DSNJobHelper dsnJobHelper;

	DocumentBuilderFactory dbfac;
	DocumentBuilder builder;
	TransformerFactory tranFactory;
	Transformer aTransformer;
	Document doc = null;

	String fileName = null;
	String xmlAllDir = null;
	
	public static void main(String args[]) {
	
	}

	public Document DOMXML(String userAccounts[]) throws IOException {
		facebookBot = new FacebookBot();
		flickrBot = new FlickrBot();
		twitterBot = new TwitterBot();

		xmlAllDir = new java.io.File(".").getCanonicalPath() + "/xmlAllData/";
		//System.out.println("xmlString====\n");
		try {
			// Create an empty xml doc
			dbfac = DocumentBuilderFactory.newInstance();
			builder = dbfac.newDocumentBuilder();
			doc = builder.newDocument();

			// Create xml tree

			// create the root element and add it to the document
			Element root = doc.createElement("user");
			doc.appendChild(root);

			// create child element, add an attribute, and add to root
			Element profile = doc.createElement("profile");
			root.appendChild(profile);

			Element services = doc.createElement("services");
			profile.appendChild(services);

			// facebook
			if (!userAccounts[1].equals("0")) {
				String facebookRealname = facebookBot.getRealName(facebookBot
						.getXmlFilename(userAccounts[1]));
				Element serviceProvider = doc.createElement("serviceProvider");
				serviceProvider.setAttribute("spName", "facebook");
				services.appendChild(serviceProvider);

				Element id = doc.createElement("id");
				serviceProvider.appendChild(id);
				Text textId = doc.createTextNode(userAccounts[1]);
				serviceProvider.appendChild(textId);

				Element realName = doc.createElement("realName");
				serviceProvider.appendChild(realName);
				Text textRealName = doc.createTextNode(facebookRealname);
				serviceProvider.appendChild(textRealName);
			}

			// flickr
			if (!userAccounts[2].equals("0")) {
				String flickrRealname = flickrBot.getRealName(flickrBot
						.getXmlFilename(userAccounts[2]));
				// System.out.println(userAccounts[2] + " exists\n");
				Element serviceProvider = doc.createElement("serviceProvider");
				serviceProvider.setAttribute("spName", "flickr");
				services.appendChild(serviceProvider);

				Element id = doc.createElement("id");
				serviceProvider.appendChild(id);
				Text textId = doc.createTextNode(userAccounts[2]);
				serviceProvider.appendChild(textId);

				Element realName = doc.createElement("realName");
				serviceProvider.appendChild(realName);
				Text textRealName = doc.createTextNode(flickrRealname);
				serviceProvider.appendChild(textRealName);
			}

			// twitter
			if (!userAccounts[3].equals("0")) {
				String twitterRealname = twitterBot.getRealName(twitterBot
						.getXmlFilename(userAccounts[3]));

				// System.out.println(userAccounts[3] + " exists\n");
				Element serviceProvider = doc.createElement("serviceProvider");
				serviceProvider.setAttribute("spName", "twitter");
				services.appendChild(serviceProvider);

				Element id = doc.createElement("id");
				serviceProvider.appendChild(id);
				Text textId = doc.createTextNode(userAccounts[3]);
				serviceProvider.appendChild(textId);

				Element realName = doc.createElement("realName");
				serviceProvider.appendChild(realName);
				Text textRealName = doc.createTextNode(twitterRealname);
				serviceProvider.appendChild(textRealName);
			}
			
			// linkedin
			if (!userAccounts[4].equals("0")) {
				String linkedinRealname = linkedinBot.getRealName(linkedinBot
						.getXmlFilename(userAccounts[4]));

				// System.out.println(userAccounts[4] + " exists\n");
				Element serviceProvider = doc.createElement("serviceProvider");
				serviceProvider.setAttribute("spName", "linkedin");
				services.appendChild(serviceProvider);

				Element id = doc.createElement("id");
				serviceProvider.appendChild(id);
				Text textId = doc.createTextNode(userAccounts[4]);
				serviceProvider.appendChild(textId);

				Element realName = doc.createElement("realName");
				serviceProvider.appendChild(realName);
				Text textRealName = doc.createTextNode(linkedinRealname);
				serviceProvider.appendChild(textRealName);
			}
			// }

			// need to update this to be the universal userId for all accounts,
			// concatenting for now
			String userAll = userAccounts[1] + "-" + userAccounts[2] + "-"
					+ userAccounts[3] + "-" + userAccounts[4];
			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();

			if(!userAccounts[1].equals("0") || !userAccounts[2].equals("0") ||
					!userAccounts[3].equals("0") || !userAccounts[4].equals("0")) {
				System.out.println("-----------------------------------");
				System.out.println("xml string to return:\n\n" + xmlString);
				this.saveToXMLFile(userAll, xmlString);
			
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return doc;
	}

	public DOMXML() {
	}

	public void saveToXMLFile(String userAll, String xmlString)
			throws IOException {
		System.out.println("save to file: " + userAll + ".txt");

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(xmlAllDir + userAll + ".txt"));
			out.write(xmlString);
			out.close();
		} catch (IOException e) {
			System.out.println("Exception ");

		}

	}
}
