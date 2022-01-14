
myCollection.stream().map(new Mapper<String,String>() {
  public String map(String input) {
    return new StringBuilder(input).reverse().toString();
  }
});

Predicate<String> isEmpty = new Predicate<String> {
    boolean test(String myString) {
        return myString.isEmpty();
    }
}
