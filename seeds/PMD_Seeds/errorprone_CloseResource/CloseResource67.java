
import java.sql.*;

public class FalsePositive {

    public void bar(Connection conn, String sqL) {
        PreparedStatement lPreparedStmt = null;
        try {
            lPreparedStmt = conn.prepareStatement(sqL);
            lPreparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println("lPreparedStmt.execute();loooooooooooooose" + ex);
        } finally {
            if (lPreparedStmt != null) {
                try {
                    lPreparedStmt.close();
                } catch (SQLException pEx) {
                    System.err.println("unrecoverable:" + pEx);
                }
            }
        }
    }
}
        