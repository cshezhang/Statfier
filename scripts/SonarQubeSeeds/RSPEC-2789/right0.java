
public void doSomething () {
  Optional<String> optional = getOptional();
  optional.ifPresent(
    // do something with optional...
  );
  Optional<String> text = Optional.empty();
  // ...
}

public Optional<String> getOptional() {
  // ...
  return Optional.empty();
}
