
public class UnusedPrivateMethodFalsePositives {

    // UnusedPrivateMethod false positive
    private void prvUnboxing(final int i)     {}
    public  void pubUnboxing(final Integer i) {prvUnboxing(i);}

    // UnusedPrivateMethod false positive
    private void prvBoxing(final Integer i) {}
    public  void pubBoxing(final int i)     {prvBoxing(i);}

    // Correctly does not generate a warning
    private void prvPrimitive(final int i) {}
    public  void pubPrimitive(final int i) {prvPrimitive(i);}

    // Correctly does not generate a warning
    private void prvObject(final Integer i) {}
    public  void pubObject(final Integer i) {prvObject(i);}
}
        