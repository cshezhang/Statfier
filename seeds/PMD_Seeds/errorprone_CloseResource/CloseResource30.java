
import java.sql.ResultSet;
import java.sql.Statement;

public class CloseResource {
    public void doSomething() {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select ...");
        try {
        } finally {
            resultSet.close();
            stmt.close();
        }
    }
}
        