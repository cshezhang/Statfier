
String date = "01/02";

Pattern datePattern = Pattern.compile("(?<month>[0-9]{2})/(?<year>[0-9]{2})");
Matcher dateMatcher = datePattern.matcher(date);

if (dateMatcher.matches()) {
  checkValidity(dateMatcher.group("month"), dateMatcher.group("year"));
}

// ...

String score = "14:1";

Pattern scorePattern = Pattern.compile("(?<player1>[0-9]+):(?<player2>[0-9]+)");
Matcher scoreMatcher = scorePattern.matcher(score);

if (scoreMatcher.matches()) {
  checkScore(scoreMatcher.group("player1"));
  checkScore(scoreMatcher.group("player2"));
}
