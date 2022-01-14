
public void doSomething () {
  Optional<String> optional = getOptional();
  if (optional != null) {  // Noncompliant
    // do something with optional...
  }
  Optional<String> text = null; // Noncompliant, a variable whose type is Optional should never itself be null
  // ...
}

@Nullable // Noncompliant
public Optional<String> getOptional() {
  // ...
  return null;  // Noncompliant
}
