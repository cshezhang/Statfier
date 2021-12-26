
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello");
        while (true) {
            sb.append("World");
            sb.append("World");
        }
        sb.append("World");
    }

    public void bar() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        while (true) {
            sb.append("World");
            sb.append("World");
        }
        sb.append("World");
    }
}
        