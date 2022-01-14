
class ParentClass {
  public boolean doSomething(){...}
}
class FirstChildClass extends ParentClass {
  public boolean doSomething(){...}  // Noncompliant
}
