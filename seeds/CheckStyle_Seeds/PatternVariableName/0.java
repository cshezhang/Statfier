class MyClass {
  MyClass(Object o1) {
    if (o1 instanceof String STRING) { // violation, name 'STRING' must
      // match pattern '^[a-z][a-zA-Z0-9]*$'
    }
    if (o1 instanceof Integer num) { // OK
    }
  }
}

