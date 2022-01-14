
public class Foo {
  static class Bar implements Comparable<Bar> {
    public int compareTo(Bar rhs) {
      return -1;
    }
  }

  static class FooBar extends Bar {
    public int compareTo(Bar rhs) {
      return 0;
    }
  }
}
