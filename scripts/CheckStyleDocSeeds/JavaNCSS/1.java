

public void test() {
  System.out.println("Line 1");
  // another 38 lines of code
  System.out.println("Line 40") // OK
  System.out.println("Line 41") // violation, the method crosses 40 non commented lines
}
        