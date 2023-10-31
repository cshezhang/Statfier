import org.owasp.esapi.ESAPI;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


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

public class XPathJavaxCustomSafe {

    public static void main(String[] args) throws Exception {

        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String input = args.length != 0 ? args[1] : "guess' or '1'='1";

        String query = "//groups/group[@id='" + XPathSuperSecureUtil.encodeForXPath(input) + "']/writeAccess/text()";

        System.out.println(">> XPath.compile()");
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(query);

            XmlUtils.printNodeList(evaluateXPath(doc, expr));
        }

        System.out.println(">> XPath.evaluate()");
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String result = xpath.evaluate(query, doc);
            System.out.println("result=" + result);
        }
    }

    public static NodeList evaluateXPath(Document doc, XPathExpression xpath) throws XPathExpressionException {
        return (NodeList) xpath.evaluate(doc, XPathConstants.NODESET);
    }


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