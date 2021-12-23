
            class Foo {

              void main(int[] bufline, int start, int bufsize) {

                int i = 0, j, k = 0;

                if ( (i = 1) > 0 || ((i = 2) < (j = i) && (j = k) == i) ) {
                    // reaching: i = 1, i = 2, j = k  (not j = i)
                } else {
                    // reaching: i = 2, j = k, j = i  (not i = 1)
                    log(j);
                    log(i);
                }
              }
            }

        