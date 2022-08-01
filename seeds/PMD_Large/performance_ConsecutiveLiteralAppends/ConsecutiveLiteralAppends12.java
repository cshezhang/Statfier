
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        while (true) {
            sb.append("World");
        }
        sb.append("World");
        for (int ix = 0; ix < 2; ix++) {
            sb.append("World");
        }
        sb.append("World");
    }

    public void bar2() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            sb.append("World");
        }
        sb.append("World");
        for (int ix = 0; ix < 2; ix++) {
            sb.append("World");
        }
        sb.append("World");
    }
}
        