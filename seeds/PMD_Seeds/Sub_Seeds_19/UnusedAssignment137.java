
            class Test {
                int a() {
                    int a = 10;
                    while (a > 0) {
                        a--;
                        try {
                            if (dummy) {
                                return somethingThatCanThrowRandomly(1);
                            } else {
                                return somethingThatCanThrowRandomly(2);
                            }
                        } catch (RuntimeException e) {
                            // retry
                        }
                    }
                    return 0;
                }
            }
            