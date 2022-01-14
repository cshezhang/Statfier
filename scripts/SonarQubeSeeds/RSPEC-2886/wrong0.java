
public class Person {
  String name;
  int age;

  public synchronized void setName(String name) {
    this.name = name;
  }

  public String getName() {  // Noncompliant
    return this.name;
  }

  public void setAge(int age) {  // Noncompliant
    this.age = age;
  }

  public int getAge() {
    synchronized (this) {
      return this.age;
    }
  }
}
