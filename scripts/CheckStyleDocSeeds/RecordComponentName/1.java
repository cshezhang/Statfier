

record MyRecord1(String value, int other) {} // OK
record MyRecord2(String... strings) {} // OK
record MyRecord3(double myNumber) {} // violation, the record component name
                             // should match the regular expression "^[a-z]+$"
        