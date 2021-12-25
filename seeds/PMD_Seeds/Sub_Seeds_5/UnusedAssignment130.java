
            import somewhere.Worker;

            class A {
                private boolean ignore = true;

                A() {
                    this(2); // this may observe `ignore = true`
                    ignore = false; // may be used by leak
                }

                A(int k) {
                    Worker.show(this); // observes `ignore = true`
                }
            }
            