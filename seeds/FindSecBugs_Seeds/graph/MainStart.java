import java.nio.file.Files;
import java.nio.file.Paths;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainStart {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String configXml = new String(Files.readAllBytes(Paths.get("/test")));
        XmlService.receiveXMLStream(configXml);
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
