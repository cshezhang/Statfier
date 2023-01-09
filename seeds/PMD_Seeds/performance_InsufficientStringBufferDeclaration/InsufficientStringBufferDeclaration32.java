
public class Foo {
    public void bar() {
        StringBuffer sb = new StringBuffer(); // initial capacity: 16
        sb.append("Hello"); // length = 5
        sb.append("World"); // length = 10
        sb.setLength(0);    // length = 0, capacity = 16
        sb.append("Hello world"); // length = 11
    }
    public void bar2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append("World");
        sb.setLength(0);
        sb.append("Hello world");
    }
}
        