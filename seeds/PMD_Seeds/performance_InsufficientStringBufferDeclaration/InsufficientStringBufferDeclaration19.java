
public class Foo {
    public void bar(String x) {
        StringBuffer sb = new StringBuffer();
        sb.append("This string" + x + "isn't nice, but valid");
    }
    public void bar2(String x) {
        StringBuilder sb = new StringBuilder();
        sb.append("This string" + x + "isn't nice, but valid");
    }
}
        