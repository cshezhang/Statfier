
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CloseResource {
    public void querySomething(Connection connection) {
        Statement stmt = null; // it complains this is not closed
        ResultSet resultSet = null; // it complains this is not closed
        String someSql = "select something...";
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executQuery(someSql);
            while (resultSet.next()) {
                // do something
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}
        