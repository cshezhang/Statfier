/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/19 7:35 PM
 */
public class CaseTest {

    public static int getLiteral1() {
        return 0;
    }

    public static void noFallthroughMethodNoDefault(int which) {
        switch (0) {
            case 0:
                doSomething();
                break;
        }
    }

    public static void doSomething() {
        System.out.println("Hello world!");
        return;
    }

}
