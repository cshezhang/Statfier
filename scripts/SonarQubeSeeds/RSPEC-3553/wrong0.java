
public String sayHello(Optional<String> name) {  // Noncompliant
  if (name == null || !name.isPresent()) {
    return "Hello World";
  } else {
    return "Hello " + name;
  }
}
