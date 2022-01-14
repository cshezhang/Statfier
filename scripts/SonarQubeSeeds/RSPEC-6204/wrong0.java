
List<String> list1 = Stream.of("A", "B", "C")
                           .collect(Collectors.toList()); // Noncompliant

List<String> list2 = Stream.of("A", "B", "C")
                           .collect(Collectors.toUnmodifiableList()); // Noncompliant
