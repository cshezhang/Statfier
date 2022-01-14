
public boolean authenticate(javax.servlet.http.HttpServletRequest request, javax.xml.xpath.XPath xpath, org.w3c.dom.Document doc) throws XPathExpressionException {
  String user = request.getParameter("user");
  String pass = request.getParameter("pass");

  String expression = "/users/user[@name=$user and @pass=$pass]";

  xpath.setXPathVariableResolver(v -> {
    switch (v.getLocalPart()) {
      case "user":
        return user;
      case "pass":
        return pass;
      default:
        throw new IllegalArgumentException();
    }
  });

  return (boolean)xpath.evaluate(expression, doc, XPathConstants.BOOLEAN);
}
