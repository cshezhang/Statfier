
assertThatThrownBy(() -> shouldThrow()).isInstanceOf(IOException.class);
//or
assertThatThrownBy(() -> shouldThrow()).hasMessage("My exception");
