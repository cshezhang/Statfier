
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("World");
        foo(sb.toString());
        sb.append("World");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("World");
        foo(sb.toString());
        sb.append("World");
    }
}
        