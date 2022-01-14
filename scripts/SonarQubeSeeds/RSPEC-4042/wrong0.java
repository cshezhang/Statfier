
public void cleanUp(Path path) {
  File file = new File(path);
  if (!file.delete()) {  // Noncompliant
    //...
  }
}
