
import java.sql.*;

final class Test {
    private Test() { }

    public static String foo(ResultSet r) throws SQLException {
        return r.getString("bar").toLowerCase();
    }
}
        