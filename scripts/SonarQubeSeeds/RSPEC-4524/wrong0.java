
switch (param) {
  case 0:
    doSomething();
    break;
  default: // default clause should be the last one
    error();
    break;
  case 1:
    doSomethingElse();
    break;
}
