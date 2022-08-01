
public class Foo {
    private Map map = new SomeMap();
    private boolean bar(Object o) {
        boolean ret = true;
        if(super.isTrue) {
            if(map.get(o) != null) {
                ret = false;
            } else {
                map.put(o,o);
            }
        }
        return ret;
    }
}
        