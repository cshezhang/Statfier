
String init = "Bob is a Bird... Bob is a Plane... Bob is Superman!";
String changed = init.replaceAll("Bob is", "It's"); // Noncompliant
changed = changed.replaceAll("\\.\\.\\.", ";"); // Noncompliant
