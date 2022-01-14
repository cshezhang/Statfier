
if ( a == a ) { // always true
  doZ();
}
if ( a != a ) { // always false
  doY();
}
if ( a == b && a == b ) { // if the first one is true, the second one is too
  doX();
}
if ( a == b || a == b ) { // if the first one is true, the second one is too
  doW();
}

int j = 5 / 5; //always 1
int k = 5 - 5; //always 0

c.equals(c); //always true
