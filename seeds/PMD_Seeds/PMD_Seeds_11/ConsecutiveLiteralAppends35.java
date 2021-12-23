
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer("CCC" + foo);
        sb.append("CCC");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder("CCC" + foo);
        sb.append("CCC");
    }
}
        