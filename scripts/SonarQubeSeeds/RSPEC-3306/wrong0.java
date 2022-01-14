
class MyComponent {  // Anyone can call the default constructor

  @Inject MyCollaborator collaborator;  // Noncompliant

  public void myBusinessMethod() {
    collaborator.doSomething();  // this will fail in classes new-ed by a caller
  }
}
