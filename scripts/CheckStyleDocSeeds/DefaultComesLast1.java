

switch (i) {
  case 1:
    break;
  case 2:
  default: // OK
    break;
  case 3:
    break;
}

switch (i) {
  case 1:
    break;
  default: // violation
  case 2:
    break;
}

// Switch rules are not subject to fall through, so this is still a violation:
switch (i) {
case 1 -> x = 9;
default -> x = 10; // violation
case 2 -> x = 32;
}
        