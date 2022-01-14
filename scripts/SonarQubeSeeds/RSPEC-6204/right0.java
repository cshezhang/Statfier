
List<String> list1 = Stream.of("A", "B", "C").toList(); // Compliant

List<String> list2 = Stream.of("A", "B", "C")
                           .collect(Collectors.toList()); // Compliant, the list2 needs to be mutable

list2.add("X");
