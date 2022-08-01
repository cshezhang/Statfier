
public class ConcurrentApp {
    public void getMyInstance() {
        Map map1 = new HashMap();   // fine for single-threaded access --- violation on this line
        Map map2 = new ConcurrentHashMap();  // preferred for use with multiple threads

        // the following case will be ignored by this rule
        Map map3 = someModule.methodThatReturnMap(); // might be OK, if the returned map is already thread-safe
    }
}
        