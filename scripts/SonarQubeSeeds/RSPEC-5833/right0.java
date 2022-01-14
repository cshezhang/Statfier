
assertThat(actual).as("Description").isEqualTo(expected);
assertThat(actual).withFailMessage("Fail message").isEqualTo(expected);
assertThat(actual).usingComparator(new CustomComparator()).isEqualTo(expected);
