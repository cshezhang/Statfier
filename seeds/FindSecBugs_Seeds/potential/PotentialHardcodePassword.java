import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.Properties;

public class PotentialHardcodePassword {

  public void badInitHardcodePassword(Properties p) {

    String riskyVal = p.getProperty("password", "ThisIsNotASecret");

    Vault v = new Vault();
    v.setPassword(riskyVal); // BAD
  }

  public void goodInitHardcodePassword(Properties p) {

    String riskyVal = p.getProperty("password");

    Vault v = new Vault();
    v.setPassword(riskyVal); // GOOD
  }

  public void badVariousSignature(
      Vault v, Properties p, Optional<String> o, HashMap<String, String> m) {
    // All the following are BAD
    v.setPassword(p.getProperty("password", "1111111"));
    v.setPassword(o.or("2222222"));
    // JDK8//v.setPassword(m.getOrDefault("password", "3333333")); //Uncomment when the project will
    // be Java8+
    v.setPassword(System.getProperty("password", "4444444"));
  }
}

class Vault {

  public void setPassword(String a) {}

  public void setParola(String b) {}

  public void setMotDePasse(String c) {}

  public void secretKey(String a) {}
}
