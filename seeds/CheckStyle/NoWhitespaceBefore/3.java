

Lists .charactersOf("foo") //violation, whitespace before '.' is not allowed
        .listIterator()
        .forEachRemaining(System.out ::print); // violation,
                                 // ^ whitespace before '::' is not allowed
Lists.charactersOf("foo")
       .listIterator()
       .forEachRemaining(System.out::print); // OK
        