
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Demo {
  private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
  private final Connection connection;

  public Demo(String serverURI) throws SQLException, ClassNotFoundException {
    Class.forName(DRIVER_CLASS_NAME); // Noncompliant; no longer required to load the JDBC Driver using Class.forName()
    connection = DriverManager.getConnection(serverURI);
  }
}
