
public ParserPool parserPool() {
  return new StaticBasicParserPool(); // Compliant: "ignoreComments" is set to "true" in StaticBasicParserPool constructor
}
