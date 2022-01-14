
assertThat(getObject()).isEqualTo(null); // Noncompliant
assertThat(getObject()).isNotEqualTo(null); // Noncompliant - not listed above but also supported

assertThat(getString().trim()).isEmpty();
assertThat(getFile().canRead()).isTrue();
assertThat(getPath().getParent()).isNull();
