
public class Foo {

    public int foo() {
        int a;
        try (Reader r = new StringReader("")) {
            a = r.read();
        } catch (IOException e) {
            a = -1;
        } catch (IllegalArgumentException e) {
            a = 2;
        }
        return a;
    }

}        