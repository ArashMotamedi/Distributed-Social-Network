package xml;

import java.io.IOException;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

public class XPathReader {
	private String xmlFile;
    private Document xmlDocument;
    private XPath xPath;
    
    public XPathReader(String xmlFile) {
        this.xmlFile = xmlFile;
        initObjects();
    }
    
    private void initObjects(){        
        try {
            xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().
			parse(xmlFile);            
            xPath =  XPathFactory.newInstance().
			newXPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }       
    }
    
    public Object read(String expression, QName returnType){
        try {
            XPathExpression xPathExpression = 
			xPath.compile(expression);
	        return xPathExpression.evaluate
			(xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
