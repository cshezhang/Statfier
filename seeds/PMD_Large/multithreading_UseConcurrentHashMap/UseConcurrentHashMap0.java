
public class Foo {
    private Map _map = new TreeMap();

    public void m() {
        Map m = new HashMap();
    }

    public Map contructMap() {
        return new HashMap(); //not detected
    }
}
        