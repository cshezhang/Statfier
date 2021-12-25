
public class Foo {
    public Object get() {
        java.math.BigDecimal bigDecimal = new java.math.BigDecimal(1);
        return bigDecimal==null ? null : bigDecimal.setScale(0, java.math.BigDecimal.ROUND_UNNECESSARY);
    }
}
         