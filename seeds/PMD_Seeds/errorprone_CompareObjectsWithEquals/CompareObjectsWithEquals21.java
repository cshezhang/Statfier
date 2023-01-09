
            class MyClass {
                static class Ts {
                    static final MyClass MISSING = new MyClass();
                }

                public static void isMissing(MyClass obj) {
                    return obj == Ts.MISSING; // no violation expected...
                }
            }
        