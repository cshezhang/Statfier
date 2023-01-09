
public class PrematureDeclarationLambda {
    public boolean lengthSumOf() {
        String signingInput = Stream.of(a, b)
                                    .filter(Objects::nonNull)
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(EMPTY));

        return Try.of(() -> sign(signingInput))
                  .getOrElse(() -> null);
    }
}
        