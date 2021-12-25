
            class Foo {

              void main(int[] bufline, int start, int bufsize) {

                int i = 0, j, k = 0;

                if ( (i = 1) > 0 && ((i = 2) < (j = i) || (j = k) == i) ) {
                    // reaching: i = 2, j = i, j = k  (not i = 1)
                    log(i);
                } else {
                    // reaching: i = 1, i = 2, j = k, j = i
                    log(j);
                }
              }
            }

        