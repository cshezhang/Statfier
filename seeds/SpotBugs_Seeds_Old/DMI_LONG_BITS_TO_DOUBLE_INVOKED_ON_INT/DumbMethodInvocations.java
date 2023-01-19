
import edu.umd.cs.findbugs.annotations.ExpectWarning;

class DumbMethodInvocations {

  @ExpectWarning("DMI_LONG_BITS_TO_DOUBLE_INVOKED_ON_INT")
  double convertToDouble(int i) {
    return Double.longBitsToDouble(i);
  }
}

