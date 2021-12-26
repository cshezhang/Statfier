
public class Foo {
    public void bar(int i) {
        StringBuffer sb = new StringBuffer(1);
        if (i == 1) {
            sb.append((char) 0x0041);
        } else if (i == 2) {
            sb.append((char) 0x0041);
        } else if (i == 19) {
            sb.append((char) 0x0041);
        } else {
            sb.append((char) 0x0041);
        }
    }
    public void bar2(int i) {
        StringBuilder sb = new StringBuilder(1);
        if (i == 1) {
            sb.append((char) 0x0041);
        } else if (i == 2) {
            sb.append((char) 0x0041);
        } else if (i == 19) {
            sb.append((char) 0x0041);
        } else {
            sb.append((char) 0x0041);
        }
    }
}
        