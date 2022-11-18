

public void foo() {
  int myInteger = 0;
  {                      // violation
    myInteger = 2;
  }
  System.out.println("myInteger = " + myInteger);

  switch (a) {
    case 1:
      {                    // violation
        System.out.println("Case 1");
        break;
      }
    case 2:
      System.out.println("Case 2");     // OK
      break;
  }
}
        