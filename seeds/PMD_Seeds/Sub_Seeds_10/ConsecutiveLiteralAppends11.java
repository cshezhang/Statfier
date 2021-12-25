
public class Foo {
    public void bar() {
        while (true) {
            StringBuffer sb = new StringBuffer();
            sb.append("World");
            sb.append("World");
        }
    }

    public void bar2() {
        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("World");
            sb.append("World");
        }
    }
}
        