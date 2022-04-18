
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append(12345);
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append(12345);
    }
}
        