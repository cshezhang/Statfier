
Pattern pattern1 = Pattern.compile("a++abc");       // Noncompliant, the second 'a' never matches
Pattern pattern2 = Pattern.compile("\\d*+[02468]"); // Noncompliant, the sub-pattern "[02468]" never matches
