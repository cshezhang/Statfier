
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  String param1 = req.getParameter("param1");

  // Replace pattern-breaking characters
  param1 = param1.replaceAll("[\n\r\t]", "_");

  Logger.info("Param1: " + param1 + " " + Logger.getName());
  // ...
}
