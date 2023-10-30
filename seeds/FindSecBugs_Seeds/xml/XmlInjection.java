package testcode.xml;

import org.apache.commons.lang.StringEscapeUtils;
import org.owasp.encoder.Encode;

public abstract class XmlInjection {

    public String badXmlStringParam(String amount) {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>" + amount
                + "</amount></product>";
        return xmlString;
    }


    public String goodXmlStringParam(String amount) {
        String xmlString = "<b><a href=\"search?amount=" + Encode.forHtmlAttribute(amount) + "\">Click Me</a></b>";
        return xmlString;
    }


    public String badXmlStringFunction1() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + unreliableAmount() + "</amount></product>";
        return xmlString;
    }

    public String badXmlStringFunction2() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>"+StringEscapeUtils.escapeHtml(unreliableAmount())+"</price>\n<amount>"
                + unreliableAmount() + "</amount></product>";
        return xmlString;
    }

    public String badXmlStringFunction3() {
        return "<product>\n<name>Cellphone</name>\n<price>"+12345+"</price>\n<amount>"
                + unreliableAmount() + "</amount></product>";
    }

    public String goodXmlStringFunction1() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + StringEscapeUtils.escapeHtml(unreliableAmount()) + "</amount></product>";
        return xmlString;
    }


    public String goodXmlStringFunction2() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + StringEscapeUtils.escapeXml(unreliableAmount()) + "</amount></product>";
        return xmlString;
    }

    public String goodXmlStringFunction3() {
        String xmlString = "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + Encode.forHtml(unreliableAmount()) + "</amount></product>";
        return xmlString;
    }

    public String goodXmlStringFunction4() {
        return "<product>\n<name>Cellphone</name>\n<price>800</price>\n<amount>"
                + 12345 + "</amount></product>";
    }

    abstract String unreliableAmount();

}
