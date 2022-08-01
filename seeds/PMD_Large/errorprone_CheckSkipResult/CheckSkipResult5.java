
            import java.io.InputStream;

            public class Foo {
                public void service(Object servletRequest) {
                    if (servletRequest instanceof InputStream nanoRequest) {
                        // ...
                    }
                }
            }
            