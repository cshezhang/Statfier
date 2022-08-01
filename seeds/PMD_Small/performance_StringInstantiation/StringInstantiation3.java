
public class Foo {
    void foo() {
        byte[] bytes = new byte[50];
        String bar = new String(bytes, 0, bytes.length, "some-encoding");
    }
}
        