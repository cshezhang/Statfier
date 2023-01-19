class Test {
  Runnable r =
      () -> { // violation, 6 lines
        System.out.println(2); // line 2 of lambda
        System.out.println(3);
        System.out.println(4);
        System.out.println(5);
      };

  Runnable r2 = () -> // violation, 6 lines
      "someString".concat("1").concat("2").concat("3").concat("4").concat("5").concat("6");

  Runnable r3 =
      () -> { // ok, 5 lines
        System.out.println(2);
        System.out.println(3);
        System.out.println(4);
      };
}

