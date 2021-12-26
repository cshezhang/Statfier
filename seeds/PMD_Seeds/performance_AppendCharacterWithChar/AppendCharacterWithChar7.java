
public class Foo {
    public void bar(StringBuffer sb) {
        sb.append("\n");
        sb.append("\t");
        sb.append("\b");
        sb.append("\r");
        sb.append("\f");
        sb.append("\\");
        sb.append("\'");
        sb.append("\"");
    }
}
        