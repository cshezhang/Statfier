
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public void runSafe(HttpServletRequest request) throws IOException {
  String folderarg1 = request.getParameter("folder");

  String cmd[] =  new String[] { "mkdir", folderarg1 };

  Runtime.getRuntime().exec(cmd); // Compliant
}
