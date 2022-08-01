
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(16);
        sb.append("foo");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder(16);
        sb.append("foo");
    }
}
        