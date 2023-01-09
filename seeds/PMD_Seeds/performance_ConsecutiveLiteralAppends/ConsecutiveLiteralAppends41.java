
public class Foo {
    public void foo(int i) {
        StringBuffer sb = new StringBuffer();
        sb.append(foo(i-2));
        sb.append("World");
    }

    public void foo2(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(foo(i-2));
        sb.append("World");
    }
}
        