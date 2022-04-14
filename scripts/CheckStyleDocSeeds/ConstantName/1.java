

class MyClass {
  final static int log = 10; // OK
  final static int logger = 50; // OK
  final static int logMYSELF = 10; // violation, name 'logMYSELF' must match
                                   // pattern '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  final static int loggerMYSELF = 5; // violation, name 'loggerMYSELF' must match
                                     // pattern '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  final static int MYSELF = 100; // OK
  final static int myselfConstant = 1; // violation, name 'myselfConstant' must match pattern
                                       // '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
}
        