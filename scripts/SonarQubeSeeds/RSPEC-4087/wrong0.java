
try (PrintWriter writer = new PrintWriter(process.getOutputStream())) {
  String contents = file.contents();
  writer.write(new Gson().toJson(new MyObject(contents)));
  writer.flush();
  writer.close();  // Noncompliant
}
