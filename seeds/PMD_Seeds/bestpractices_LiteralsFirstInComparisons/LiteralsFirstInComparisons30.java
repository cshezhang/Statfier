
import java.math.BigInteger;

public class LiteralsFirstInComparisonBigInteger {
    public void foo() {
        BigInteger value = new BigInteger("1");
        if (value.equals(BigInteger.ZERO)) {
            System.out.println("1==0!!");
        }
    }
}
        