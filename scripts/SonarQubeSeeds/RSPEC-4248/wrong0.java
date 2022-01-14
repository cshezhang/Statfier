
public void doingSomething(String stringToMatch) {
  Pattern regex = Pattern.compile("myRegex");  // Noncompliant
  Matcher matcher = regex.matcher("s");
  // ...
  if (stringToMatch.matches("myRegex2")) {  // Noncompliant
    // ...
  }
}
