
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// to be compliant, completely disable DOCTYPE declaration:
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
// or completely disable external entities declarations:
factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
// or prohibit the use of all protocols by external entities:
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");


SAXParserFactory factory = SAXParserFactory.newInstance();
// to be compliant, completely disable DOCTYPE declaration:
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
// or completely disable external entities declarations:
factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
// or prohibit the use of all protocols by external entities:
SAXParser parser = factory.newSAXParser(); // Noncompliant
parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

XMLInputFactory factory = XMLInputFactory.newInstance();
// to be compliant, completely disable DOCTYPE declaration:
factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
// or completely disable external entities declarations:
factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
// or prohibit the use of all protocols by external entities:
factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
// to be compliant, prohibit the use of all protocols by external entities:
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
// to be compliant, completely disable DOCTYPE declaration:
factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
// or prohibit the use of all protocols by external entities:
factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
