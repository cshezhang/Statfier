
public class Foo {
    String bar() {
        try {
        } finally {
            Object o = new Object() {
                @Override
                public String toString() {
                    return "";
                }
            };
        }
    }
}
        