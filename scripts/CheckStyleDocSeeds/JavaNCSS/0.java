

public void test() {
  System.out.println("Line 1");
  // another 48 lines of code
  System.out.println("Line 50") // OK
  System.out.println("Line 51") // violation, the method crosses 50 non commented lines
}
        