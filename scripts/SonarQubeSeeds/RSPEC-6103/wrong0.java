
assertThat(myObject).isInstanceOfSatisfying(String.class, s -> "Hello".equals(s)); // Noncompliant - not testing the string value
assertThat(myObject).satisfies("Hello"::equals); // Noncompliant - not testing the string value
