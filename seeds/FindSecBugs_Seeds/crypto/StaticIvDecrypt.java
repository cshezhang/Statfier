import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class StaticIvDecrypt {

  public static void decrypt(String message, SecretKey secretKey, byte[] iv) throws Exception {
    // IV
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    // Encrypt
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
    cipher.update(message.getBytes());

    byte[] data = cipher.doFinal();
    System.out.println(HexUtil.toString(data));
  }
}

class HexUtil {

  public static String toString(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X ", b));
    }
    return sb.toString();
  }
}
