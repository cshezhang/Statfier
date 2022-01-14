
public void doSomethingWithFile(String fileName) {
  BufferedReader buffReader = null;
  try {
    buffReader = new BufferedReader(new FileReader(fileName));
    String line = null;
    while ((line = buffReader.readLine()) != null) {
      // ...
    }
  } catch (IOException e) {
    // ...
  }
}
