
Integer i = 123456789;
Float f = 1.0f;
Number n = condition ? i : f;  // Noncompliant; i is coerced to float. n = 1.23456792E8
