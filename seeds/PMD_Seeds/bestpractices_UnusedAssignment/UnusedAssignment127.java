
            import somewhere.Worker;

            class A {
                private boolean ignore = true;  // used

                A() {
                    ignore = false; // used
                }

                private Worker worker = new Worker(this.foo()); // there is a leak here

                A foo() { return null; } // is virtual
            }
            