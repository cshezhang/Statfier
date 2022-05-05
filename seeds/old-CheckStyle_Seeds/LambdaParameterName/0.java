

Function<String, String> function1 = s -> s.toLowerCase(); // OK
Function<String, String> function2 = S -> S.toLowerCase(); // violation, name 'S'
                                               // must match pattern '^[a-z][a-zA-Z0-9]*$'
        