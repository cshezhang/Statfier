
public class Foo {
    public Foo() {
        Map<String, double[]> map = new LinkedHashMap<>();
        addToMap(map);
    }

    private void addToMap(Map<String, double[]> map) {
        map.put("foo", new double[]{0., 1.});
    }
}
        