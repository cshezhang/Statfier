
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(1);
        sb.append((char) 0x0041);
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder(1);
        sb.append((char) 0x0041);
    }
}
        