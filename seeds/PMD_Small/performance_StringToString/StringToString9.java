
public class Foo {
    private void log(Supplier<String> msg) { }
    public void run() {
        String abc = "abc";
        log(abc::toString); // fails rule
        log(() -> abc); // passes rule
    }
}
        