

int[][] array = { { 1, 2 }
                , { 3, 4 } }; // OK, linebreak is allowed before ','
int[][] array2 = { { 1, 2 },
                   { 3, 4 } }; // OK, ideal code
void ellipsisExample(String ...params) {}; // violation, whitespace before '...' is not allowed
void ellipsisExample2(String
                        ...params) {}; //OK, linebreak is allowed before '...'
Lists.charactersOf("foo")
       .listIterator()
       .forEachRemaining(System.out::print); // OK
        