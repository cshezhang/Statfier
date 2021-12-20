package MutatorTestingCases;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Case10 {

    final String str = "hardcoded initial vector";
    public void func(SecretKeySpec key) {
        try {
            byte[] ivBytes, ivs;
            ivs = str.getBytes();
            final IvParameterSpec iv = new IvParameterSpec(ivs); // should report a warning about this line
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            // .......
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
