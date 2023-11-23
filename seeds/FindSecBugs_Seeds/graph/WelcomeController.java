import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WelcomeController {

  @GetMapping("/test")
  public String direct(@RequestParam("xml") String xml) throws Exception {
    XmlService.receiveXMLStream(xml);
    return "/test";
  }

  @GetMapping("/deep")
  public String deep(@RequestParam("xml") String xml) throws Exception {
    callMe(xml);
    return "/test";
  }

  public void callMe(String xml) throws IOException, SAXException, ParserConfigurationException {
    XmlService.receiveXMLStream(xml);
  }
}

class XmlService {
  public static void receiveXMLStream(final String xml)
      throws ParserConfigurationException, SAXException, IOException {
    // ...
    InputStream inStream = new ByteArrayInputStream(xml.getBytes());
    SAXParserFactory spf = SAXParserFactory.newInstance();
    final SAXParser saxParser = spf.newSAXParser();
    saxParser.parse(inStream, new DefaultHandler());
  }
}
