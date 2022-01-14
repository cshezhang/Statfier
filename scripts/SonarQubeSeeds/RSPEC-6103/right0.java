
assertThat(myObject).isInstanceOfSatisfying(String.class, s -> assertThat(s).isEqualTo("Hello"));
assertThat(myObject).satisfies(obj -> assertThat(obj).isEqualTo("Hello"));
