package MutatorTestingCases;

import javax.crypto.spec.SecretKeySpec;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/12/20 4:02 下午
 */
public class Case12 {
    void encrypt() {
        SecretKeySpec keySpec =  new SecretKeySpec("hard coded key here".getBytes(), "AES");
    }
}
