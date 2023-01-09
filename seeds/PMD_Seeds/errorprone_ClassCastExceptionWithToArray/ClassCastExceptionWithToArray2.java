
public class Foo {
    String bar(Vector vec) {
        return (String)vec.toArray().toString();
    }
    String foo(Vector vec) {
        return (String)vec.toArray()[0];
    }
}
        