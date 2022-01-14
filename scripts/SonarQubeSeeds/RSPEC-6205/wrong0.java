
int i = switch (mode) {
  case "a" -> {        // Noncompliant: Remove the redundant block and yield.
    yield 1;
  }
  default -> {         // Noncompliant: Remove the redundant block and yield.
    yield 2;
  }
};

switch (mode) {
  case "a" -> {        // Noncompliant: Remove the redundant block and break.
    result = 1;
    break;
  }
  default -> {         // Noncompliant: Remove the redundant break.
    doSomethingElse();
    result = 2;
    break;
  }
}
