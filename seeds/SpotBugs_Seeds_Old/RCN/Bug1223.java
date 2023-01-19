import edu.umd.cs.findbugs.annotations.ExpectWarning;
import org.w3c.dom.Element;

public class Bug1223 {

  @ExpectWarning("RCN")
  public static boolean check(Element e) {
    if (e.getAttribute("x") == null) return true;
    return false;
  }

  @ExpectWarning("RCN")
  public static boolean check2(Element e) {
    if (e.getAttributeNS("x", "y") == null) return true;
    return false;
  }
}

