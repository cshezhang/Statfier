package iter0;

public class A {
    private java.lang.Object[] content;
    // This method should not be a violation of MethodReturnsInternalArray
    private java.lang.Object[] getContent() {
        return content;
    }
}
        