
class ParentClass {
  public boolean doSomething(){...}
}
class FirstChildClass extends ParentClass {
  @Override
  public boolean doSomething(){...}  // Compliant
}
