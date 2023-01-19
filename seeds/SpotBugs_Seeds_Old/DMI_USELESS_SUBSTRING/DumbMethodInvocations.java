
import edu.umd.cs.findbugs.annotations.ExpectWarning;

class DumbMethodInvocations {

  @ExpectWarning("DMI_USELESS_SUBSTRING")
  String f(String s) {
    return s.substring(0);
  }
}

