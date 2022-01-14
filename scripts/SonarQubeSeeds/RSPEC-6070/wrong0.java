
Pattern.compile("\\ca"); // Noncompliant, 'a' is not an upper case letter
Pattern.compile("\\c!"); // Noncompliant, '!' is outside of the '@'-'_' range
