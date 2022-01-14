
/** src/main/java/MyObject.java */

@VisibleForTesting String foo;

/** src/main/java/Service.java */

new MyObject().foo; // Noncompliant, foo is accessed from production code
