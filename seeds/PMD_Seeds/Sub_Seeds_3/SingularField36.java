
public class Source {
    @Deprecated
    private Object o; //violation!

    Object m() {
        o = new Object();
        return o;
    }
}
        