
Arrays.sort(rosterAsArray,
    (a, b) -> {  // Noncompliant
        return a.getBirthday().compareTo(b.getBirthday());
    }
);
