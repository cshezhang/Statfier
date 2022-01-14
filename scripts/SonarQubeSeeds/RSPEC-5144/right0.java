
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  String urlWhiteListed = "https://example.com/";
  String str = req.getParameter("url");
  if (!str.startsWith(urlWhiteListed))
    throw new IOException();

  URL url2 = new URL(str);
  HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection(); // compliant
}
