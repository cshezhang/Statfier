
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

public class test {

    public static void main(String[] args) {
      try
      {
        Cipher c31 = Cipher.getInstance("AES/GCM/NoPadding"); // Compliant
      }
      catch(NoSuchAlgorithmException|NoSuchPaddingException e)
      {
      }
    }
}
