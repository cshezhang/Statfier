
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer(30);
        sb.append("foo").append("this is presized just right");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder(30);
        sb.append("foo").append("this is presized just right");
    }
}
        