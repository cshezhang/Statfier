int foo(int x, int y) {
 return x + y;
}


void invariant_hoist(int size) {
    int x = 10;
    int y = 5;
    for (int i = 0; i < size; i++) {
      foo(x, y); // hoistable
    }
  }