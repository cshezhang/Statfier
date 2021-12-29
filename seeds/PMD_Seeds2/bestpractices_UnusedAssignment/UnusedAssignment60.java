
            import java.io.IOException;
            import java.io.Reader;
            import java.io.StringReader;

            public class Foo {

                public int foo() {
                    int a = 0;
                    try (Reader r = new StringReader("")) {
                        try (Reader r = new StringReader("")) {
                            a = r.read();       // overwritten in finally
                        } finally {
                            a = 0; // overwritten in enclosing catch, if `read()` threw, otherwise in enclosing finally
                        }
                    } catch (IOException e) {
                        a = -1; // overwritten in finally
                    } finally {
                        a = 1;
                    }
                    return a;
                }

            }
        