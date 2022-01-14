
            class MyClass {
                static final MyClass MISSING = new MyClass();

                public static void isMissing(MyClass obj) {
                    return obj == MISSING; // no violation expected...
                }
            }
        