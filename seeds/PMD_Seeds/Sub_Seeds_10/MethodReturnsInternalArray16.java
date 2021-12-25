
public class MethodReturnsInternalArrayCase {
    private static final byte[] DATA = new byte[0];

    protected final byte[] getData()
    {
        return DATA;
    }
}
        