/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/19 7:35 PM
 */
public class CaseTest {

    static int a;
    int e = 5;

    public void d() {
        a = 10;
        System.out.println(e);
    }

    static {
        a = 19;
    }

    Object o = new Object() {
        int c = 10;
    };



    enum enumClass {
        RED;
        int d = 10;
    }

    public CaseTest() {
        int c = 10;
    }

    static {
        int b = a;
    }

    public void foo() {
        int b = 10;
    }

}
