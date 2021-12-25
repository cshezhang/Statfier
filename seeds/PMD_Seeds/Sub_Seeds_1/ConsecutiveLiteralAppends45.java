
public class Foo {
    public void foo(int i) {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello");
        sb.append(i + 1);
        sb.append(1 + i);
        sb.append("World");
    }

    public void foo2(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(i + 1);
        sb.append(1 + i);
        sb.append("World");
    }
}
        