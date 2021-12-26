
            import somewhere.Worker;

            class A extends Worker { // extends some unknown class
                private boolean ignore = true;  // unused

                A() {
                    // implicit super call, which may observe the default value of ignore
                    ignore = false; // may be used by leak
                }
            }
            