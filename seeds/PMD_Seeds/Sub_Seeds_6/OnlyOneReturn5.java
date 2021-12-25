
public class Foo {
    public int foo() {
        FileFilter f = new FileFilter() {
            public boolean accept(File file) {
                return false;
            }
        };
        return 2;
    }
}
        