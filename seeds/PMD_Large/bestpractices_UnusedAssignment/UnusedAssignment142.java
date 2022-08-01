
            class Test {
                static int b = 0;
                Test() {
                    System.out.println(b); // does not count as usage
                }
                static {
                    b = 2;
                }
            }
            