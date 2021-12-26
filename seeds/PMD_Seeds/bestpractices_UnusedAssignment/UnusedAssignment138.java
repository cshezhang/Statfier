
            class Test {
                int field;
                void foo(Test t) {
                    t.field = 2 * t.field;
                    if (t.field > 5) {
                        t.field = 5;
                    }
                }
            }
            