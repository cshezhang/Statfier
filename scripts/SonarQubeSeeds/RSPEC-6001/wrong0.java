
Pattern.compile("\\1(.)"); // Noncompliant, group 1 is defined after the back reference
Pattern.compile("(.)\\2"); // Noncompliant, group 2 isn't defined at all
Pattern.compile("(.)|\\1"); // Noncompliant, group 1 and the back reference are in different branches
Pattern.compile("(?<x>.)|\\k<x>"); // Noncompliant, group x and the back reference are in different branches
