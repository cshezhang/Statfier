
public class CombinersTest {

    private static final BinaryOperator<Purse> ADDITION = (p1, p2) -> {
        p1.amount += p2.amount;
        return p1;
    };

    private static class Purse {
        private int amount;

        public Purse(final int amount) {
            this.amount = amount;
        }
    }
}
        