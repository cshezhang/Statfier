
var myVariable = "var";

int minusOne(int i) {
  return switch (i) {
    case 1: yield(0);
    default: yield(i-1);
  };
}

String myRecord = "record";
