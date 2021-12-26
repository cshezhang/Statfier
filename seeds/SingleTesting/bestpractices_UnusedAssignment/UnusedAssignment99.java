
class Foo {

  void main(int[] bufline, int start, int bufsize) {

    int i = 0, j, k = 0;

    if (  (i = 2) < (j = i)
     ||   (j = k) == i       ) {

        // reaching: i = 2, j = i, j = k
        log(j);
    } else {
        // reaching: i = 2, j = k  (not j = i)
    }
  }
}

        