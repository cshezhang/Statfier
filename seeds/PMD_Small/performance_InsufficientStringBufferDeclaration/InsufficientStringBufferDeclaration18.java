
public class Foo {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("This string" + " " + "isn't nice, but valid");
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("This string" + " " + "isn't nice, but valid");
    }
}
        