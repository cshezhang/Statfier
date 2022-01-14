
if ("red".equals(choice)) {  // Noncompliant
  dispenseRed();
} else if ("blue".equals(choice)) {
  dispenseBlue();
} else if ("yellow".equals(choice)) {
  dispenseYellow();
} else {
  promptUser();
}
