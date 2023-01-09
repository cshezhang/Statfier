
public class Foo extends Bar {
    public BigDecimal getBalance(Date date) {
        return super.getBalance(date).negate();
    }
}

class Bar {
    public BigDecimal getBalance(Date date) {
    }
}
        