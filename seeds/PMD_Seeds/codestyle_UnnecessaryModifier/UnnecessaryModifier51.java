
public class Foo {
    public void stuff() {
        try (final FileWriter fw = new FileWriter(new File("something.txt"))) {
            // do something on fw
        }
    }
}
        