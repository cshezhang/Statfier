
if (x != null && x instanceof MyClass) { ... }  // Noncompliant

if (x == null || ! x instanceof MyClass) { ... } // Noncompliant
