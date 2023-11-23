import java.util.Properties;
import javax.crypto.Cipher;

public class PotentialAlgorithm {

  public void testDesAlgo(Properties config) throws Exception {
    String algorithmWithBadDefault = config.getProperty("algorithm", "DES");
    Cipher.getInstance(algorithmWithBadDefault);
  }
}
