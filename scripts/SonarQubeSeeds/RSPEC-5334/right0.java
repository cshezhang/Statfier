
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
  String input = req.getParameter("input");

  // Match the input against a whitelist
  if (!whiteList.contains(input))
    throw new IOException();

  ScriptEngineManager manager = new ScriptEngineManager();
  ScriptEngine engine = manager.getEngineByName("JavaScript");
  engine.eval(input);
}
