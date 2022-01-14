
record Person(String name, int age) { } // Compliant

record Person(String name, int age) {
  Person(String name, int age) { // Compliant
    this.name = name.toLowerCase(Locale.ROOT);
    this.age = age;
  }
}

record Person(String name, int age) {
  Person { // Compliant
    if (age < 0) {
      throw new IllegalArgumentException("Negative age");
    }
  }
  public String name() { // Compliant
    return name.toUpperCase(Locale.ROOT);
  }
}
