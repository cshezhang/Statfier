
public class Foo {
    public void foo() {
        @NotNull
        BigDecimal bd = new BigDecimal(5);
        bd.divideToIntegralValue(new BigDecimal(5));
    }
}
        