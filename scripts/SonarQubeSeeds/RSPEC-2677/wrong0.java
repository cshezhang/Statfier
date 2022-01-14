
public void doSomethingWithFile(String fileName) {
  BufferedReader buffReader = null;
  try {
    buffReader = new BufferedReader(new FileReader(fileName));
    while (buffReader.readLine() != null) { // Noncompliant
      // ...
    }
  } catch (IOException e) {
    // ...
  }
}
