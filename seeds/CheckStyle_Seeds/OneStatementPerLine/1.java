

OutputStream s1 = new PipedOutputStream();
OutputStream s2 = new PipedOutputStream();
// only one statement(variable definition) with two variable references
try (s1; s2; OutputStream s3 = new PipedOutputStream();) // OK
{}
// two statements with variable definitions
try (Reader r = new PipedReader(); s2; Reader s3 = new PipedReader() // violation
) {}
        