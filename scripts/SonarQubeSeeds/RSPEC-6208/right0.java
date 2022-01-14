
// Switch Expression
int i = switch (mode) {
  case "a", "b":
    yield 1;
  default:
    yield 3;
};

// Switch Statement
switch (mode) {
  case "a", "b":
    doSomething();
    break;
  default:
    doSomethingElse();
}

// Or even better:
switch (mode) {
  case "a", "b" -> doSomething();
  default -> doSomethingElse();
}
