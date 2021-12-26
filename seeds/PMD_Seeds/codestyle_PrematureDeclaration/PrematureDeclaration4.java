
public class PrematureDeclarationLambda {
    public int lengthSumOf() {
        int sum = 0;
        Runnable r = () -> { return; };
        r.run();
        return sum;
    }
}
        