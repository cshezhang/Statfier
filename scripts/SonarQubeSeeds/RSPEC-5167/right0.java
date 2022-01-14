
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String value = req.getParameter("value");

    String whitelist = "safevalue1 safevalue2";
    if (!whitelist.contains(value))
      throw new IOException();

    resp.addHeader("X-Header", value); // Compliant
}
