

int foo;
foo ++; // violation, whitespace before '++' is not allowed
foo++; // OK
for (int i = 0 ; i < 5; i++) {}  // violation
           // ^ whitespace before ';' is not allowed
for (int i = 0; i < 5; i++) {} // OK
int[][] array = { { 1, 2 }
                , { 3, 4 } }; // violation, whitespace before ',' is not allowed
int[][] array2 = { { 1, 2 },
                   { 3, 4 } }; // OK
Lists.charactersOf("foo").listIterator()
       .forEachRemaining(System.out::print)
       ; // violation, whitespace before ';' is not allowed
{
  label1 : // violation, whitespace before ':' is not allowed
  for (int i = 0; i < 10; i++) {}
}

{
  label2: // OK
  while (true) {}
}
        