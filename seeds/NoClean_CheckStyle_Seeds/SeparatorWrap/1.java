

import java.util.Arrays;

class Test2 {

  String[] stringArray = {"foo", "bar"};

  void fun() {
    Arrays.sort(stringArray, String::
      compareToIgnoreCase);  // violation, separator method reference on same line
    Arrays.sort(stringArray, String
      ::compareTo);  // OK
  }

}
        