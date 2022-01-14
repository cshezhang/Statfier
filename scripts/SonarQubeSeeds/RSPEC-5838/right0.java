
assertThat(getObject()).isNull();

assertThat(getString()).isBlank();
assertThat(getFile()).canRead();
assertThat(getPath()).hasNoParentRaw();
