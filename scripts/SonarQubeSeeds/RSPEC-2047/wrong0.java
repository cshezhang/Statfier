
public boolean getFoo() { // Noncompliant
  // ...
}

public boolean getBar(Bar c) { // Noncompliant
  // ...
}

public boolean testForBar(Bar c) { // Compliant - The method does not start by 'get'.
  // ...
}
