import edu.umd.cs.findbugs.annotations.ExpectWarning;
import java.applet.Applet;
import java.net.URL;

public class BadApplet extends Applet {
  @ExpectWarning("Dm")
  public BadApplet() {
    URL u1 = getDocumentBase();
    URL u2 = getCodeBase();

    if (u1.equals(u2)) return;

    if (getParameter("bad") != null) return;

    if (getAppletContext() != null) return;
  }
}

