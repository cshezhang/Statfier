
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(foo + "CCC");
        sb.append("CCC");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder(foo + "CCC");
        sb.append("CCC");
    }
}
        