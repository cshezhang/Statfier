
public class PrematureDeclarationLambda {
    public boolean lengthSumOf() {

        return Try.of(() -> {
            // Inside that lambda *is* a premature declaration of `sum`
            int sum = 0;

            if (strings == null || strings.length == 0)
                return 0;

            for (int i = 0; i < strings.length; i++) {
                sum += strings[i].length();
            }

            return sum;
        });
    }
}
        