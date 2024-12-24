import static java.lang.System.out;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorMessages {
  public void vulnerableErrorMessage1() {
    try {
      Connection conn = DriverManager.getConnection("jdbc:mysql://prod.company.com/production");
    } catch (SQLException sqlException) {
      sqlException.printStackTrace(out); // Normal Priority
    }
  }

  public void vulnerableErrorMessage2(HttpServletRequest req, HttpServletResponse resp) {
    try {
      OutputStream out = resp.getOutputStream();
    } catch (Exception e) {
      e.printStackTrace(out); // Normal Priority
    }
  }

  public void vulnerableErrorMessage3() {
    FileInputStream fis = null;
    try {
      String fileName = "fileName";
      fis = new FileInputStream(fileName);
    } catch (Exception e) {
      e.printStackTrace(); // Low Priority
    }
  }
}