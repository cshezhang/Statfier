
public class Foo {
    public void bar(String str1, String str2) {
        StringBuffer buf = new StringBuffer(str1.length() + str2.length());
        buf.append(str1);
        buf.append(str2);
    }
}
        