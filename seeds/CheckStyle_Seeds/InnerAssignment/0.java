

class MyClass {

  void foo() {
    int a, b;
    a = b = 5; // violation, assignment to each variable should be in a separate statement
    a = b += 5; // violation

    a = 5; // OK
    b = 5; // OK
    a = 5; b = 5; // OK

    double myDouble;
    double[] doubleArray = new double[] {myDouble = 4.5, 15.5}; // violation

    String nameOne;
    List<String> myList = new ArrayList<String>();
    myList.add(nameOne = "tom"); // violation

    for (int k = 0; k < 10; k = k + 2) { // OK
      // some code
    }

    boolean someVal;
    if (someVal = true) { // violation
      // some code
    }

    while (someVal = false) {} // violation

    InputStream is = new FileInputStream("textFile.txt");
    while ((b = is.read()) != -1) { // OK, this is a common idiom
      // some code
    }
  }

  boolean testMethod() {
    boolean val;
    return val = true; // violation
  }
}
        