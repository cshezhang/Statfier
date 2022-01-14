
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  String location = req.getParameter("url");

  List<String> allowedHosts = new ArrayList<String>();
  allowedUrls.add("https://www.domain1.com/");
  allowedUrls.add("https://www.domain2.com/");

  if (allowedUrls.contains(location))
    resp.sendRedirect(location); // Compliant
}
