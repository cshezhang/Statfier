
public class Foo {
    void foo() {
        for(String[] bar : bars()) {
            bar[0] = "foo";
            bar.length = 5; // let's assume that array.length is writable...
            this.bar = 5;
        }
    }
}
        