
public class RuleViolator {
    public BigInteger foo() {
        // some boiler plate code
        final BigInteger anImmutable = BigInteger.ZERO;
        final BigInteger anotherImmutable = BigInteger.ONE;
        BigInteger unrelated = BigInteger.valueOf(42);

        // the actual PMD problem occurs with the next statement
        if (anImmutable.add(BigInteger.TEN).compareTo(anotherImmutable) == 0) {
            // do something here that is not related to the actual comparison in
            // the if clause
            unrelated = unrelated.multiply(BigInteger.TEN);
        }

        return unrelated;
    }
}
         