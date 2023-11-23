import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class XPathApacheXPathApi {

  public static void main(String[] args) throws Exception {
    Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

    String input = args.length != 0 ? args[1] : "guess' or '1'='1";
    String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

    // selectNodeIterator
    NodeIterator iterator = XPathAPI.selectNodeIterator(doc, query);
    XmlUtils.printNodeIterator(iterator);

    // selectNodeList
    NodeList nodeList = XPathAPI.selectNodeList(doc, query);
    XmlUtils.printNodeList(nodeList);

    // selectSingleNode
    Node node = XPathAPI.selectSingleNode(doc, query);
    XmlUtils.printNode(node);

    // Static string (safe)
    Node node2 =
        XPathAPI.selectSingleNode(
            doc, "//groups/group[@id='guess']/writeAccess/text()".toLowerCase());
    XmlUtils.printNode(node2);
  }
}

class XmlUtils {

  public static Document loadDoc(String path)
      throws ParserConfigurationException, IOException, SAXException {
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
