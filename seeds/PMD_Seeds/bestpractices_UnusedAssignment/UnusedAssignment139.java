
            class Test {
                static {
                    int b = 0;
                    int d = 0;
                    {
                        d = ++b;
                    }
                }
            }
            