class MyClass {
  static final int log = 10; // OK
  static final int logger = 50; // OK
  static final int logMYSELF = 10; // violation, name 'logMYSELF' must match
  // pattern '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  static final int loggerMYSELF = 5; // violation, name 'loggerMYSELF' must match
  // pattern '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
  static final int MYSELF = 100; // OK
  static final int myselfConstant = 1; // violation, name 'myselfConstant' must match pattern
  // '^log(ger)?$|^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$'
}

