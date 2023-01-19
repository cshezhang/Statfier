import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Case9 {

  Connection getConnection1() throws SQLException {
    return DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", ""); // to submit
  }
}

