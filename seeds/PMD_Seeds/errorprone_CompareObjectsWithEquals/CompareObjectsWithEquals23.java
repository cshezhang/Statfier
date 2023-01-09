
            class MyClass {
                static class Ts {
                    final MyClass MISSING = new MyClass();
                    Ts id() { return this; }
                }

                public static void isMissing(MyClass obj, Ts ts) {
                    return obj == (ts.id()).id().MISSING; // no violation expected...
                }
            }
        