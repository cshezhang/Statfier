class Test {
  public static void main(String[] args) {
    int a = 0;
    a =
        switch (i) { // this is used
            // all those are unused
          case 1 -> a = 1;
          case 2 -> {
            if (a > 0) {
              yield a++;
            }
            yield 4;
          }
          case 3 -> a = 3;
          default -> a = a + 1;
        };

    System.out.println(a);
  }
}

