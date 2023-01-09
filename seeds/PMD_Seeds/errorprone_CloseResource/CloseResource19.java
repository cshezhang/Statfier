
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CloseResourceRuleBug {
    public void foo() {
        try {
            Connection c = DriverManager.getConnection("fake");
            Statement s = c.createStatement();
            @SuppressWarnings("PMD.CloseResource") ResultSet rs = s.executeQuery("fake");
            while (rs.next()) {
            }
            rs.close();
        } catch (SQLException e) {
        }
    }
}
        