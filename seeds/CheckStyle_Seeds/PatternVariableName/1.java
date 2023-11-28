class MyClass {
  MyClass(Object o1) {
    if (o1 instanceof String STR) { // violation, name 'STR' must
      // match pattern '^[a-z](_?[a-zA-Z0-9]+)*$'
    }
    if (o1 instanceof Integer num) { // OK
    }
    if (o1 instanceof Integer num_1) { // OK
    }
  }
}

