

/** @see Math */ // violation, javadoc should be multiline
public int foo() {
  return 42;
}

/**
 * @return 42
 */  // ok
public int bar() {
  return 42;
}

/** {@link #equals(Object)} */ // ok
public int baz() {
  return 42;
}

/**
 * <p>the answer to the ultimate question
 */ // ok
public int magic() {
  return 42;
}

/**
 * <p>the answer to the ultimate question</p>
 */ // ok
public int foobar() {
  return 42;
}
        