
import javax.crypto.spec.SecretKeySpec;

public class Foo {

    void encrypt() {
        byte[] computedSecretKey = SecureUtils.computeSecretKey(data.getSecretKeyAlgorithm(),
            data.getAccessKey(), data.getClientIp(), data.getTimeStamp(), data.getEnv());
        SecretKeySpec secretKeySpec = new SecretKeySpec(computedSecretKey, data.getSecretKeyAlgorithm().getName());
    }
}
        