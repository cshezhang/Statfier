/**
 * @Description: A testing case for adding final modifier to method arguments
 * @Author: Huaien Zhang
 * @Date: 2021-08-18 13:37
 */
public class AddFinalToArgumentTesting {

    public void testA(String str) {
        str += "AAA";
        System.out.println(str);
    }

    public void testB(String str, int num) {
        System.out.println(str + " " + num);
    }

    public static void testC(String str) {
        System.out.println(str);
    }

    public static void testD(double d) {
        System.out.println(d);
    }

    public static void main(String[] args) {
        AddFinalToArgumentTesting tester = new AddFinalToArgumentTesting();
        tester.testA("123");
        tester.testA("456");
        tester.testB("789", 1);
        testC("10");
        testD(10.0d);
    }

}
