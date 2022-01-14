
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  String name = req.getParameter("name");
  String encodedName = org.owasp.encoder.Encode.forHtml(name);
  PrintWriter out = resp.getWriter();
  out.write("Hello " + encodedName);
}
