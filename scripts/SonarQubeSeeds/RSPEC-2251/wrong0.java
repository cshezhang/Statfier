
public void doSomething(String [] strings) {
  for (int i = 0; i < strings.length; i--) { // Noncompliant;
    String string = strings[i];  // ArrayIndexOutOfBoundsException when i reaches -1
    //...
  }
