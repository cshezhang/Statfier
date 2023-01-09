
            class MyClass {
                static class Ts {
                    final MyClass MISSING = new MyClass();
                }

                public static void isMissing(MyClass obj, Ts ts) {
                    return obj == ts.MISSING; // no violation expected...
                }
            }
        