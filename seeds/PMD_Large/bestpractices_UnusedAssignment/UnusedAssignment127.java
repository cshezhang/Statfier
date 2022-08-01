

            class Worker {
                private final Worker.Listener listener;

                Worker(Listener listener) {
                    this.listener = listener;
                    work();
                }

                void work() {listener.onWork();}

                interface Listener { void onWork(); }
            }

            class A implements Worker.Listener {
                private boolean ignore;
                private Worker worker;

                A() {
                    ignore = false; // actually unused
                    ignore = true;  // may be observed by the leak
                    worker = new Worker(this); // leak

                    // This could technically be observed by another thread (not sure, maybe the field needs to be volatile too)
                    // This looks like a very rare circumstance though.
                    // So we say it's unused
                    ignore = false;

                    ignore = false; // this exits the ctor so may be used later
                }

                void doWork() { worker.work(); }

                public void onWork() {
                    if (ignore) {
                        return;
                    }
                    System.out.println("onWork");
                }
            }

            