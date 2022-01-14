class ERADICATE_FIELD_NOT_NULLABLE {
  String f;

  void foo(@Nullable String s) {
    f = s;
  }
}