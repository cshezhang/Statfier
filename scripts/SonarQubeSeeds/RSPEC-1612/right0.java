
class A {
  void process(List<A> list) {
    list.stream()
      .filter(B.class::isInstance)
      .map(B.class::cast)
      .map(B::<String>getObject)
      .forEach(System.out::println);
  }
}

class B extends A {
  <T> T getObject() {
    return null;
  }
}
