

class A {

   class Nested {

   }; // OK, nested type declarations are ignored

}; // violation

interface B {

}; // violation

enum C {

}; // violation

{@literal @}interface D {

}; // violation
        