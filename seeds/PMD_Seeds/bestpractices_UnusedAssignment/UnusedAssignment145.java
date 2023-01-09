
            import static java.lang.System.arraycopy;
            import static java.util.Arrays.fill;

            public class RadixSort {
                private static final int TEN = 10;
                int[] a;

                void countSort(int exp) {
                    int[] count = new int[TEN];
                    fill(count, 0);
                    for (int val : a) // error flagged here
                        count[(val / exp) % TEN]++;

                }
            }
            