
private static final Pattern myRegex = Pattern.compile("myRegex");
private static final Pattern myRegex2 = Pattern.compile("myRegex2");

public void doingSomething(String stringToMatch) {
  Matcher matcher = myRegex.matcher("s");
  // ...
  if (myRegex2.matcher(stringToMatch).matches()) {
    // ...
  }
}
