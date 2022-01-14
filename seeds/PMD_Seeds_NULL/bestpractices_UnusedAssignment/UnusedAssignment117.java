

            import static unknown.K.*;

            class Foo {
                void method(int i) {
                    while (true) {
                        switch (i) {
                        case 19:
                            try {
                                i++;
                                break;
                            } catch (RuntimeException e) { // warn
                                break;
                            } catch (Error e) { // warn
                                return;
                            }
                        case 20:
                            try {
                                i++;
                                break;
                            } catch (RuntimeException e) { // warn
                            } catch (Error e) { // warn
                                return;
                            }
                        case 21:  //fallthrough
                            try {
                                i++;
                            } catch (RuntimeException e) { // warn
                                i--;
                            } finally {
                                break;
                            }
                        case 22:
                            try {
                                i++;
                                break;
                            } catch (RuntimeException e) { // warn
                                i--;
                                break;
                            } finally {
                                i++;
                            }
                        default:  //warn
                            // this is the last label
                            i++;
                        }
                    }
                }
            }

        