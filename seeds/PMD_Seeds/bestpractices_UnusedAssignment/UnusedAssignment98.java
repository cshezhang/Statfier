
class Foo {

  void main(int[] bufline, int start, int bufsize) {

    int i = 0, j, k = 0;

    while (i < bufline.length
        // this is OR
        || bufline[j = start % bufsize] == bufline[k = ++start % bufsize]) {

      // here j, k might be their initializers
      bufline[j] = bufline[k];
      i++;
    }
  }
}

        