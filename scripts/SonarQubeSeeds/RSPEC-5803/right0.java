
/** src/main/java/MyObject.java */

@VisibleForTesting String foo;

/** src/test/java/MyObjectTest.java */

new MyObject().foo; // Compliant, foo is accessed from test code
