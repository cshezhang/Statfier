
var var = "var"; // Noncompliant: compiles but this code is confusing
var = "what is this?";

int yield(int i) { // Noncompliant
  return switch (i) {
    case 1: yield(0); // This is a yield from switch expression, not a recursive call.
    default: yield(i-1);
  };
}

String record = "record"; // Noncompliant
