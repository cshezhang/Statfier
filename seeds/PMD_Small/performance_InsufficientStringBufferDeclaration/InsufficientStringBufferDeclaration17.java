
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer(3);
        sb.append('a');
        sb.append('a');
        sb.append('a');
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder(3);
        sb.append('a');
        sb.append('a');
        sb.append('a');
    }
}
        