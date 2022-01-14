
Stream.of("one", "two", "three", "four")
         .filter(e -> e.length() > 3)
         .peek(e -> System.out.println("Filtered value: " + e)); // Noncompliant
