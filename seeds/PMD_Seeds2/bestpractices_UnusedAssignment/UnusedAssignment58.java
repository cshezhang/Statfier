
public class Foo {

    public int foo() {
        int a = 0;
        try (Reader r = new StringReader("")) {
            a = r.read();  // used in return
        } catch (IOException e) {
            a = -1; // used in return
        } finally {
            // don't use a
        }
        return a;
    }

}        