
import java.sql.*;

public class CloseResourceSQL {
    public void run() {
        try {
            Connection con = DataSourceUtils.getConnection(ds);
            PreparedStatement ps = con.prepareStatement("some SQL statement");
            DataSourceUtils.applyTransactionTimeout(ps, ds);
        } catch (SQLException ex) {
            throw new RuntimeException("", ex);
        }
    }
}
        