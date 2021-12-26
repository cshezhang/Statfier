
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer(15);
        String foo = "Hello";
        String foo2 = "World";
        sb.append(foo);
        sb.append(foo2);
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder(15);
        String foo = "Hello";
        String foo2 = "World";
        sb.append(foo);
        sb.append(foo2);
    }
}
        