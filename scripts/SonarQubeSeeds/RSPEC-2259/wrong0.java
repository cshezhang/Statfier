
@CheckForNull
String getName(){...}

public boolean isNameEmpty() {
  return getName().length() == 0; // Noncompliant; the result of getName() could be null, but isn't null-checked
}
