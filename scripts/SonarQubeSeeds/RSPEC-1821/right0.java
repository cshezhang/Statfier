
void foo(int n, int m) {
  switch (n) {
    case 0:
      bar(m);
    case 1:
      // ...
    default:
      // ...
  }
}

void bar(int m){
  switch(m) {
    // ...
  }
}
