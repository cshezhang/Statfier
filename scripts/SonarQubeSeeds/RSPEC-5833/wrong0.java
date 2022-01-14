
assertThat(actual).isEqualTo(expected).as("Description"); // Noncompliant
assertThat(actual).isEqualTo(expected).withFailMessage("Fail message"); // Noncompliant
assertThat(actual).isEqualTo(expected).usingComparator(new CustomComparator()); // Noncompliant
