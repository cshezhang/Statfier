
            import java.io.Reader;
            import java.io.StringReader;
            import java.io.IOException;

            public class Foo {

                public int foo() {
                    int a = 0;
                    try (Reader r = new StringReader("")) {
                        a = r.read(); // might assign or fail
                        a = r.read(); // might assign or fail
                    } catch (IOException e) {
                    }
                    return a;
                }

            }
            