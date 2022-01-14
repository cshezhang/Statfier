
            public final class Fib {
                /** utility class */
                private Fib() {}

                public static int fib(final int n) {
                    Preconditions.checkArgument(n >= 0);

                    if (n < 2) {
                        return n;
                    } else {
                        int a = 0;
                        int b = 1;
                        final int m = n - 1;

                        for (int i = 0; i < m; i++) {
                            final int c = a;
                            a = b;
                            b = c + b;
                        }

                        return b;
                    }
                }
            }
        