
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer(15);
        sb.append("Hello");
    }

    public void bar2() {
        StringBuffer sb = new StringBuffer(15);
        sb.append("Hello");
    }

    public void bar3() {
        StringBuilder sb = new StringBuilder(15);
        sb.append("Hello");
    }

    public void bar4() {
        StringBuilder sb = new StringBuilder(15);
        sb.append("Hello");
    }
}
        