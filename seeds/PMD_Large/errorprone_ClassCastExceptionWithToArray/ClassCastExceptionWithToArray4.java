
public class Test {
    public static E[] toArrayWrapped(Collection c) {
        // although you won't get a classcastexception here on the following line
        E[] retVal = (E[]) c.toArray();
        // you'll get it later on, when you try to use the return value.
        // Depending on the Collection implementation, this method
        // still returns an Object[] array type, and not E[].
        return retVal;
    }
}
        