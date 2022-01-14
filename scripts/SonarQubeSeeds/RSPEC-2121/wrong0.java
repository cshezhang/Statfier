
String speech = "Now is the time for all good people to come to the aid of their country.";

String s1 = speech.substring(0); // Noncompliant. Yields the whole string
String s2 = speech.substring(speech.length()); // Noncompliant. Yields "";
String s3 = speech.substring(5,speech.length()); // Noncompliant. Use the 1-arg version instead

if (speech.contains(speech)) { // Noncompliant
 // always true
}
