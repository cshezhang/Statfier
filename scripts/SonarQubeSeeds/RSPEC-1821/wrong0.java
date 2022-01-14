
void foo(int n, int m) {
  switch (n) {
    case 0:
      switch (m) {  // Noncompliant; nested switch
        // ...
      }
    case 1:
      // ...
    default:
      // ...
  }
}
