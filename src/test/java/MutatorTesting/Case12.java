package MutatorTestingCases;

import javax.crypto.spec.SecretKeySpec;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/12/20 4:02 下午
 */
public class Case12 {
    void encrypt() {
        SecretKeySpec keySpec =  new SecretKeySpec("hard coded key here".getBytes(), "AES");
    }
}
