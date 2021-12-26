
public class A {
    private Object[] content;
    // This method should not be a violation of MethodReturnsInternalArray
    private Object[] getContent() {
        return content;
    }
}
        