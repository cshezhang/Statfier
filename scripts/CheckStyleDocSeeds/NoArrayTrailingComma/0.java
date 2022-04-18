

String[] foo1 = {
  "FOO", // OK
  "BAR", // violation
};
String[] foo2 = { "FOO", "BAR", }; // violation
String[] foo3 = {
  "FOO", // OK
  "BAR" // OK
};
String[] foo4 = { "FOO", "BAR" }; // OK
        