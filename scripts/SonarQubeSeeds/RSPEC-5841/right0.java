
List<String> logs = getLogs();

assertThat(logs).isNotEmpty().allMatch(e -> e.contains(“error”));
// Or
assertThat(logs).hasSize(5).allMatch(e -> e.contains(“error”));
// Or
assertThat(logs).isEmpty();

// Despite being redundant, this is also acceptable since it explains why you expect an empty list
assertThat(logs).doesNotContain("error").isEmpty();
// or test the content of the list further
assertThat(logs).contains("warning").doesNotContain("error");
