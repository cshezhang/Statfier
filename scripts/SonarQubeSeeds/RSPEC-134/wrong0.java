
if (condition1) {                  // Compliant - depth = 1
  /* ... */
  if (condition2) {                // Compliant - depth = 2
    /* ... */
    for(int i = 0; i < 10; i++) {  // Compliant - depth = 3, not exceeding the limit
      /* ... */
      if (condition4) {            // Noncompliant - depth = 4
        if (condition5) {          // Depth = 5, exceeding the limit, but issues are only reported on depth = 4
          /* ... */
        }
        return;
      }
    }
  }
}
