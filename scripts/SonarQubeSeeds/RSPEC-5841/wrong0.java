
List<String> logs = getLogs();

assertThat(logs).allMatch(e -> e.contains(“error”)); // Noncompliant, this test pass if logs are empty!
assertThat(logs).doesNotContain("error"); // Noncompliant, do you expect any log?
