
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        String foo = "Hello";
        sb.append("Hello");
        sb.append("World");
        sb.append(foo);
        sb.append("World");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        String foo = "Hello";
        sb.append("Hello");
        sb.append("World");
        sb.append(foo);
        sb.append("World");
    }
}
        