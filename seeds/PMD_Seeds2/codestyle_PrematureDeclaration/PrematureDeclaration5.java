
public class PrematureDeclarationLambda {
    public int lengthSumOf(String[] foo) {

        int snafoo = 0;

        if (foo == null || foo.length == 0)
            return 0;

        for (String aFoo : foo) {
            snafoo += aFoo.length();
        }

        return snafoo;
    }
}
        