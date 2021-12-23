
public class Foo {
    public void bar(int a, int b, long c, long d) {
        StringBuffer buf = new StringBuffer();
        buf.append(a + b);
        buf.append(c - d);

        String str1 = "a";
        String str2 = "b";
        StringBuffer sb = new StringBuffer(str1.length() + str2.length());
    }
}
        