
class Foo {

  void main(int[] bufline, int start, int bufsize) {

    int i = 0, j, k = 0;

    while (i < bufline.length
        // this is AND
        && bufline[j = start % bufsize] == bufline[k = ++start % bufsize]) {

      bufline[j] = bufline[k];
      i++;
    }
  }
}

        