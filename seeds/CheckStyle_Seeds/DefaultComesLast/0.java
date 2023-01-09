

switch (i) {
  case 1:
    break;
  case 2:
    break;
  default: // OK
    break;
}

switch (i) {
  case 1:
    break;
  case 2:
    break; // OK, no default
}

switch (i) {
  case 1:
    break;
  default: // violation, 'default' before 'case'
    break;
  case 2:
    break;
}

switch (i) {
  case 1:
  default: // violation, 'default' before 'case'
    break;
  case 2:
    break;
}
        