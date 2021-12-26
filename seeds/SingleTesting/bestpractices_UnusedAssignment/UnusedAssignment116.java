

            import static unknown.K.*;

            class Foo {
                {
                    for (String s : someExpression()) {
                        try {
                            foo(s);
                        } catch (Exception e) {
                            print("failure");
                        }

                    }
                }
            }

        