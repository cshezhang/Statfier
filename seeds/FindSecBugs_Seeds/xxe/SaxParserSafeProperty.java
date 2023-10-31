import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class SaxParserSafeProperty {

    public static File getInputFile() {
        return new File("C:/Code/evil.xml"); ///tmp/user/upload_123.xml
    }

    private static void print(Document doc) {
        System.out.println(doc.getFirstChild().toString());
    }

    public static void unsafeNoSpecialSettings() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();

        parser.parse(getInputFile(), new PrintHandler());
    }


    public static void safeIgnoredDtdDisable() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        SAXParser parser = spf.newSAXParser();

        parser.parse(getInputFile(), new PrintHandler());
    }

    public static void safeSecureProcessing() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        SAXParser parser = spf.newSAXParser();

        parser.parse(getInputFile(), new PrintHandler());
    }

    public static void safeManualConfiguration() throws ParserConfigurationException, IOException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://xml.org/sax/features/external-general-entities", true);
        spf.setFeature("http://xml.org/sax/features/external-parameter-entities", true);
        spf.setXIncludeAware(false);
        SAXParser parser = spf.newSAXParser();

        parser.parse(getInputFile(), new PrintHandler());
    }

    public static void main(String[] args) throws Exception {
        //unsafeNoSpecialSettings();
        safeIgnoredDtdDisable(); //Throw SAXParseException: DOCTYPE is disallowed when the feature [...]
        //safeSecureProcessing();
        //safeManualConfiguration();
    }
}

class PrintHandler extends DefaultHandler {
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        System.out.println("Node = " + qName);
    }

    public void characters(char ch[], int start, int length)
            throws SAXException {

        System.out.println("New content received");
        System.out.println(new String(ch).substring(start, start + length));
    }
}