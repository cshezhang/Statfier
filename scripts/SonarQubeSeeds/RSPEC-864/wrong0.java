
x = a + b - c;
x = a + 1 << b;  // Noncompliant
y = a == b ? a * 2 : a + b;  // Noncompliant

if ( a > b || c < d || a == d) {...}
if ( a > b && c < d || a == b) {...}  // Noncompliant
if (a = f(b,c) == 1) { ... } // Noncompliant; == evaluated first
