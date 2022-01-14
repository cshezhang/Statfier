
try (
    FileReader fr = new FileReader(fileName);
    BufferedReader br = new BufferedReader(fr)
  ) {
  return br.readLine();
}
catch (...) {}
