package PMDCases;

import javax.crypto.spec.SecretKeySpec;

/**
 * Description: Manual testing for PMD
 * Author: Vanguard
 * Date: 2021/10/7 3:28 PM
 */
public class PMDCase1 extends Object {

    public enum enumClazz {
        sub_EnumClazz {
            public String toString() {
                return "test";
            }
            public void notOverride() {
                System.out.println("111");
            }
        };
        public String toString() {
            return "111";
        }
        public void notOverride() {
            System.out.println("111");
        }
    }

    public String toString() {
        return "111";
    }

    void encrypt_8() {
        final String var0;
        var0 = "hard coded key here";
        final String var1 = "AES";
        SecretKeySpec keySpec =  new SecretKeySpec(var0.getBytes(), var1);
    }

    void encrypt_42() {
        final String var0 = "hard coded key here";
        final String var1 = "AES";
        SecretKeySpec keySpec =  new SecretKeySpec(var0.getBytes(), var1);
    }

    public static void main(String[] args) {
        PMDCase1 tester = new PMDCase1();
        tester.encrypt_8();
        tester.encrypt_42();
    }

}
