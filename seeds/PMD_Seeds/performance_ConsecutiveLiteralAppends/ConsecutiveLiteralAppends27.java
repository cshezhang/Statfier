
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("CCC");
        if (true) {
            sb.append("CCC");
        } else if (sb.length() == 2) {
            sb.append("CCC");
        } else {
            sb.append("CCC");
        }
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("CCC");
        if (true) {
            sb.append("CCC");
        } else if (sb.length() == 2) {
            sb.append("CCC");
        } else {
            sb.append("CCC");
        }
    }
}
        