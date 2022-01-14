
switch (myVariable) {
  case 0: // Noncompliant: 6 lines till next case
    methodCall1("");
    methodCall2("");
    methodCall3("");
    methodCall4("");
    break;
  case 1:
  ...
}
