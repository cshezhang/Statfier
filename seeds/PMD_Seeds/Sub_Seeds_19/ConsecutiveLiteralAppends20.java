
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        String foo = "AAA";
        sb.append(foo).append("BBB");
        sb.append("CCC");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        String foo = "AAA";
        sb.append(foo).append("BBB");
        sb.append("CCC");
    }
}
        