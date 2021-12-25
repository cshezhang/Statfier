
import java.sql.ResultSet;
import java.sql.Statement;

public class Foo {
    void bar() {
        ResultSet rs = null;
        Statement stmt = getConnection().createStatement();
        try {
            rs = stmt.getResultSet();
            rs.getString(0);
            rs.close();
        } catch (Exception e) {
        } finally {
            stmt.close();
        }
    }
}
        