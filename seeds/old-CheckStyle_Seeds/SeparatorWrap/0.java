

import java.io.
         IOException; // OK

class Test {

  String s;

  public void foo(int a,
                    int b) { // OK
  }

  public void bar(int p
                    , int q) { // violation, separator comma on new line
    if (s
          .isEmpty()) { // violation, separator dot on new line
    }
  }

}
        