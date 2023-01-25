public class ASTCase {
    private String[] foobar1 = new String[0];
    public class subClass1367 {
        public final String[] call1() {
            return foobar1;
        }
    }
}
class Foo {
    public Foo() {
        final boolean var196 = true;
        bar(var196);
    }
    public void bar(boolean b) {}
}