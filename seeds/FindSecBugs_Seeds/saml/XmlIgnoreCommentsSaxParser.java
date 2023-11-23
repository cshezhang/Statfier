import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlIgnoreCommentsSaxParser {

  private static void receiveXMLStream(final InputStream inStream, final DefaultHandler defHandler)
      throws ParserConfigurationException, SAXException, IOException {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    final SAXParser saxParser = spf.newSAXParser();
    saxParser.parse(inStream, defHandler);
  }

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    InputStream is =
        XmlIgnoreCommentsSaxParser.class.getResourceAsStream("/testcode/xml/simple.xml");
    receiveXMLStream(is, new BufferHandler());
  }
}

class PrintHandler extends DefaultHandler {
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    System.out.println("Node = " + qName);
  }

  public void characters(char ch[], int start, int length) throws SAXException {
    System.out.println("New content received");
    System.out.println(new String(ch).substring(start, start + length));
  }
}

class BufferHandler extends DefaultHandler {

  private String currentNodeName = null;
  private StringBuilder buffer = new StringBuilder();

  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    buffer = new StringBuilder();
    // System.out.println("Node = " + qName);
    currentNodeName = qName;
  }

  public void characters(char ch[], int start, int length) throws SAXException {

    // System.out.println("New content received");

    buffer.append(ch, start, length);
  }

  public void endElement(String uri, String localName, String qName) throws SAXException {
    System.out.println(qName + "=" + buffer.toString());
    buffer = new StringBuilder();
  }
}
