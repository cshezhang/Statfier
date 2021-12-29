
public class PrematureDeclarationLambda {
    public boolean lengthSumOf() {
        String foo = "";
        return new ArrayList<String>().stream().anyMatch(bar -> foo.equals(bar));
    }
}
        