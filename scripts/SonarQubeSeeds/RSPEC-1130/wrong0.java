
void foo() throws MyException, MyException {}  // Noncompliant; should be listed once
void bar() throws Throwable, Exception {}  // Noncompliant; Exception is a subclass of Throwable
