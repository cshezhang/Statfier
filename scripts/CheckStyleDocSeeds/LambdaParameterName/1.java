

class MyClass {
  Function<String, String> function1 = str -> str.toUpperCase().trim(); // OK
  Function<String, String> function2 = _s -> _s.trim(); // violation, name '_s'
                                             // must match pattern '^[a-z]([a-zA-Z]+)*$'

  public boolean myMethod(String sentence) {
    return Stream.of(sentence.split(" "))
            .map(word -> word.trim()) // OK
            .anyMatch(Word -> "in".equals(Word)); // violation, name 'Word'
                                   // must match pattern '^[a-z]([a-zA-Z]+)*$'
  }
}
        