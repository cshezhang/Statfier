
/**
 * Description:
 * Author: Vanguard
 * Date: 2021/8/24 8:05 PM
 */

public class TransferLocalVarToGlobalTesting {

    public static void main() {
        int a = 10;
        System.out.println(a);
        final int b = 10;
        final double c = 100;
        System.out.println(c);
    }
}
