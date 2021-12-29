
public class Foo {
    public void foo(String[] a, int i) {
        StringBuffer sb = new StringBuffer();
        sb.append(a[i]).append("Hello");
        sb.append(a[i+1]).append("World");
    }

    public void foo2(String[] a, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(a[i]).append("Hello");
        sb.append(a[i+1]).append("World");
    }
}
        