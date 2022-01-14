
public interface DefaultMethodInInterface {
    default String toString(Object one) {
        return toString(one, one);
    }
    String toString(Object one, Object two);

    default Object justReturn(Object o) {
        return o;
    }
}
        