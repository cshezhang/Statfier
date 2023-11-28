package Test;

import static Test.MyClass.*; // OK, static import
// OK, static import

// violation, imported from the same package as the current package
// violation, the class imported is from the 'java.lang' package
// OK
// violation, it is a duplicate of another import

public class MyClass {}
;

