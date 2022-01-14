
Arrays.asList("a1", "a2", "b1", "c2", "c1").stream()
    .filter(...)
    .forEach(...);

Arrays.asList(1, 2, 3, 4).stream() // Noncompliant
    .filter(...)
    .forEach(...);
