
// Switch Expression
int i = switch (mode) {
  case "a":
  case "b":
    yield 1;
  default:
    yield 3;
};

// Switch Statement
switch (mode) {
  case "a":
  case "b":
    doSomething();
    break;
  default:
    doSomethingElse();
}
