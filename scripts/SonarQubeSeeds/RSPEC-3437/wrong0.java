
class MyClass implements Serializable {
  private HijrahDate date;  // Noncompliant; mark this transient
  // ...
}
