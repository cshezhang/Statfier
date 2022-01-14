
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Demo {
    private final Connection connection;

    public Demo(String serverURI) throws SQLException {
        connection = DriverManager.getConnection(serverURI);
    }
}
