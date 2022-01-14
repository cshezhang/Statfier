
public class Student {  // no hashCode() method; not hash-able
  // ...

  public boolean equals(Object o) {
    // ...
  }
}

public class School {
  private Map<Student, Integer> studentBody = // okay so far
          new HashTable<Student, Integer>(); // Noncompliant

  // ...
