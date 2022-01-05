/**
 * @Description: This is the testing case for data flow propagation.
 * @Author: Huaien Zhang
 * @Date: 2021-08-16 17:23
 */
public class AddRedundantLiteralTesting {

    public void testA() {
        int a = 10;
        int b;
        b = 10;
    }

    public void testB() {
        double a = 10.0;
    }

}
