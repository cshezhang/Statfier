
            import java.io.IOException;
            import java.io.Reader;
            import java.io.StringReader;

            class Foo {

                public int foo() {
                    int a;
                    try (Reader r = new StringReader("")) {
                        a = r.read(); // really unused: overwritten with r.read() and 0;
                        a = r.read(); // might assign or fail
                    } catch (IOException e) {
                        a = 0;
                    }
                    return a;
                }

            }
            