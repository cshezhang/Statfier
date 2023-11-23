import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;

public class ObjectDeserializationFalsePositive2 {
  public static void main(String[] args) throws IOException {

    new ASN1InputStream().readObject();
  }
}
