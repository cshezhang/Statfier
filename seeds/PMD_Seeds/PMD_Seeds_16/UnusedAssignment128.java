
            import somewhere.Worker;

            class A {
                private boolean ignore = true;  // unused

                A() {
                    ignore = false; // may be used by leak

                    if (Worker.something()) {
                        worker = new Worker(this.foo()); // marks the reaching defs as used bc of leak
                    } else {
                        worker = null;
                        ignore = true; // there is no leak in this branch
                    }
                    ignore = true;
                }
            }
            