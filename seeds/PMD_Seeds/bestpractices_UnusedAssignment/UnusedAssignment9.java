public class Foo {
  void bar(int i) {
    int j = 0; // unused
    int z = 0;
    if (i < 10) {
      j = i; // unused
      throw new Exception();
    } else {
      j = z;
    }
    System.out.println(j);
  }
}

