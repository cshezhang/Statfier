
public class Foo {
    List<BigDecimal> getSolution() {
        List<BigDecimal> result = new ArrayList<BigDecimal>();
        for (int i = 0; i < size(); i++) {
           result.add(entry(size(),i).negate());
           result.add(this.equations[i].check(solution));
        }
    }
}
        