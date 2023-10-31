import org.apache.xml.security.utils.JDKXPathAPI;
import org.apache.xml.security.utils.XPathAPI;
import org.apache.xml.security.utils.XalanXPathAPI;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.xpath.XPath;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public abstract class XPathApacheXmlSec {


    public void main(@RequestParam("test") String input) throws Exception {
        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

        XPathAPI api1 = getXPathAPI();

        api1.evaluate(null,null,query,null);
        api1.selectNodeList(null,null,query,null);


        JDKXPathAPI api2 = getJDKXPathAPI();

        api2.evaluate(null,null,query,null);
        api2.selectNodeList(null,null,query,null);

        XalanXPathAPI api3 = getXalanXPathAPI();

        api3.evaluate(null,null,query,null);
        api3.selectNodeList(null,null,query,null);
    }

    public abstract XPathAPI getXPathAPI();
    public abstract JDKXPathAPI getJDKXPathAPI();
    public abstract XalanXPathAPI getXalanXPathAPI();
}

class XmlUtils {

    public static Document loadDoc(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFactory.newDocumentBuilder();

        InputStream in = XmlUtils.class.getResourceAsStream(path);
        return builder.parse(in);
    }

    public static void printNode(Node node) {
        System.out.println(node.getNodeValue());
    }

    public static void printNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNodeValue());
        }
    }

    public static void printNodeIterator(NodeIterator iterator) {
        Node n;
        while ((n = iterator.nextNode()) != null) {
            System.out.println("Node:" + n.getNodeValue());
        }
    }
}
