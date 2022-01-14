
final class Person { // Noncompliant
  private final String name;
  private final int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {...}

  public int getAge() {...}

  @Override
  public boolean equals(Object o) {...}

  @Override
  public int hashCode() {...}

  @Override
  public String toString() {...}
}
