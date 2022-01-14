
String date = "01/02";

Pattern datePattern = Pattern.compile("(?<month>[0-9]{2})/(?<year>[0-9]{2})");
Matcher dateMatcher = datePattern.matcher(date);

if (dateMatcher.matches()) {
  checkValidity(dateMatcher.group(1), dateMatcher.group(2));  // Noncompliant - numbers instead of names of groups are used
  checkValidity(dateMatcher.group("day")); // Noncompliant - there is no group called "day"
}

// ...

String score = "14:1";

Pattern scorePattern = Pattern.compile("(?<player1>[0-9]+):(?<player2>[0-9]+)"); // Noncompliant - named groups are never used
Matcher scoreMatcher = scorePattern.matcher(score);

if (scoreMatcher.matches()) {
  checkScore(score);
}
