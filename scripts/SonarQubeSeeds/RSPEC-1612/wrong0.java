
class A {
  void process(List<A> list) {
    list.stream()
      .filter(a -> a instanceof B)
      .map(a -> (B) a)
      .map(b -> b.<String>getObject())
      .forEach(b -> { System.out.println(b); });
  }
}

class B extends A {
  <T> T getObject() {
    return null;
  }
}
