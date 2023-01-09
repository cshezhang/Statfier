
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(100);
        sb.append("Hello");
        sb.append("World");
        sb.setLength(0);
        sb.append("Hello world");
        sb.append("Hello world");
        sb.append("Hello world");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Hello");
        sb.append("World");
        sb.setLength(0);
        sb.append("Hello world");
        sb.append("Hello world");
        sb.append("Hello world");
    }
}
        