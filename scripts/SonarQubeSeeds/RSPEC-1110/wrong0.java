
int x = (y / 2 + 1);   //Compliant even if the parenthesis are ignored by the compiler

if (a && ((x+y > 0))) {  // Noncompliant
  //...
}

return ((x + 1));  // Noncompliant
