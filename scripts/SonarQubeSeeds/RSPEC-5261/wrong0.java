
 if (a)
   if (b)
     d++;
 else     // Noncompliant, is the "else" associated with "if(a)" or "if (b)"? (the answer is "if(b)")
   e++;
