
Stream.of("one", "two", "three", "four")
         .filter(e -> e.length() > 3)
         .foreach(e -> System.out.println("Filtered value: " + e));
