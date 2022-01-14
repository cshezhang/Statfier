
Arrays.asList(1, 2, 54000).stream().collect(Collectors.toMap(Function.identity(), ArrayList::new)); // Noncompliant, "ArrayList::new" unintentionally refers to "ArrayList(int initialCapacity)" instead of "ArrayList()"
