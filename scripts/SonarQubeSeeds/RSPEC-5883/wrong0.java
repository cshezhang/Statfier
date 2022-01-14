
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public void runUnsafe(HttpServletRequest request) throws IOException {
  String folder = request.getParameter("folder");

  String cmd = "mkdir " + folder;

  Runtime.getRuntime().exec(cmd); // Noncompliant
}
