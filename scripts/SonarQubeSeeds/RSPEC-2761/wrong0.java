
int i = 1;

int j = - - -i;  // Noncompliant; just use -i
int k = ~~~i;    // Noncompliant; same as i
int m = + +i;    // Noncompliant; operators are useless here

boolean b = false;
boolean c = !!!b;   // Noncompliant
