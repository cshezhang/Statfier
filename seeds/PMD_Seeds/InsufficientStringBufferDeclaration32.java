package iter0;

public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello");
        sb.append("World");
        sb.setLength(0);
        sb.append("Hello world");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append("World");
        sb.setLength(0);
        sb.append("Hello world");
    }
}
        