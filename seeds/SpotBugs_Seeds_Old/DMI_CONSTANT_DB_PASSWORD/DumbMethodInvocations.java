import edu.umd.cs.findbugs.annotations.ExpectWarning;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DumbMethodInvocations {

  @ExpectWarning("DMI_CONSTANT_DB_PASSWORD")
  Connection getConnection2() throws SQLException {
    return DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "secret");
  }
}

