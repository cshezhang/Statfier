
int i = switch (mode) {
  case "a" -> 1;
  default -> 2;
};

switch (mode) {
  case "a" -> result = 1;
  default -> {
   doSomethingElse();
   result = 2;
 }
}
