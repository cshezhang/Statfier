
public class Student {  // has hashCode() method; hash-able
  // ...

  public boolean equals(Object o) {
    // ...
  }
  public int hashCode() {
    // ...
  }
}

public class School {
  private Map<Student, Integer> studentBody = new HashTable<Student, Integer>();

  // ...
