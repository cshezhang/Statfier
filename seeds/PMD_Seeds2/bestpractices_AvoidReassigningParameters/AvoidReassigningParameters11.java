
public class Foo {
    public Foo(int arg, String arg2, Object arg3) {
        arg = arg + 3;
        arg += arg + "some other string";
        arg3 = arg3.clone();
    }
}
        