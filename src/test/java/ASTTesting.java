import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/9/23 9:08 下午
 */
public class ASTTesting {

        public void testA() {
            System.out.println("10");
            String a = "20";
            testB(a);
        }

        public static void testB(String str) {
            str = str + str;
            System.out.println(str);
        }

        public void testC(int c) {
            int a = 4;
            String b = "1111";
            System.out.println(a + c);
            System.out.println(b);
        }

        public void testD(int a) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(a);
            a += 10;
            list.add(a);
            for(int i : list) {
                System.out.println(i);
            }
        }

        public void bugA(final String key0) {
            final String key1 = "key";
            SecretKeySpec keySpec0 = new SecretKeySpec(key0.getBytes(), "AES");
            SecretKeySpec keySpec1 = new SecretKeySpec(key1.getBytes(), "AES");
            System.out.println(keySpec0);
            System.out.println(keySpec1);
        }

        public void bugB() {
            String key = "hardcoded key";
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            System.out.println(keySpec);
        }

        public static void main(String[] args) {
            ASTTesting tester = new ASTTesting();
            tester.testA();
            tester.testC(8);
            tester.testD(32);
            tester.bugA("secret key");
            tester.bugB();
        }

}
