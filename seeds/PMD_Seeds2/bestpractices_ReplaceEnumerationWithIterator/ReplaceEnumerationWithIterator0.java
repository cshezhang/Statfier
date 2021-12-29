
public class Foo implements Enumeration {
    public boolean hasMoreElements() {
        return true;
    }
    public Object nextElement() {
        return "hello";
    }
}
        