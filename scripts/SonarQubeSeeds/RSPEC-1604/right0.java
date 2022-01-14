
myCollection.stream().map(input -> new StringBuilder(input).reverse().toString());

Predicate<String> isEmpty = myString -> myString.isEmpty();
