class Test {
  public static void main(String[] args) {
    int a = 0;
    int i = 0;
    switch (i) {
      case 1 -> a = 1;
      case 2 -> {
        if (args.length > 0) {
          i = 4;
          break;
        }
        a = 2;
      }
      case 3 -> a = 3;
      default -> a = a + 1;
    }
    System.out.println(a);
  }
}

