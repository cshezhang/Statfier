
public class UnnecessaryLocalBeforeReturnFP {
    public int example1() {
        int i = compute(); // might throw
        markComputationDone();
        return i; // PMD complains that variable could be avoided
    }

    public int example2() {
        Mutable m = new Mutable();
        int i = compute(m);
        sideEffect(m);
        return i;
    }
}
        