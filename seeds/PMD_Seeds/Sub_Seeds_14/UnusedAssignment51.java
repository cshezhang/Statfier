
public class Foo {

    public int foo() {
        int a = 0;
        try (Reader r = new StringReader("")) {
            a = r.read();
        } catch (IOException e) {
            a = -1;
        }
        return a;
    }

}        