
boolean result1 = foo.toUpperCase().equals(bar);             // Noncompliant
boolean result2 = foo.equals(bar.toUpperCase());             // Noncompliant
boolean result3 = foo.toLowerCase().equals(bar.LowerCase()); // Noncompliant
