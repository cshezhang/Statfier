
String firstName = getFirstName(); // String overrides equals
String lastName = getLastName();

if (firstName == lastName) { ... }; // Non-compliant; false even if the strings have the same value
