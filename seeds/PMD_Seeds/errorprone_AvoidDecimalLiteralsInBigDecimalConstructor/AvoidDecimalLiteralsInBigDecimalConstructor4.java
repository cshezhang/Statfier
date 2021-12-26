
import java.math.BigDecimal;

public class Foo {
    public void bar() {
        double d = 0.1;
        BigDecimal bd = new BigDecimal(d); // line 6
    }
    public void bar2() {
        float f = 0.1f;
        BigDecimal bd = new BigDecimal(f); // line 10
    }
    public BigDecimal bar3(double e) {
        return new BigDecimal(e); // line 13
    }
    public BigDecimal bar4(float g) {
        return new BigDecimal(g); // line 16
    }
    public void bar5() {
        Double h = 0.1;
        BigDecimal bd = new BigDecimal(h); // line 20
    }
    public void bar6() {
        Float k = 0.1f;
        BigDecimal bd = new BigDecimal(k); // line 24
    }
    public BigDecimal bar7(Double m) {
        return new BigDecimal(m); // line 27
    }
    public BigDecimal bar8(Float n) {
        return new BigDecimal(n); // line 30
    }
}
        