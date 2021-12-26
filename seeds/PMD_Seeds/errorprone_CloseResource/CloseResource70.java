
import java.sql.*;
import static java.util.Objects.*;

public class FalsePositive {

    public void bar(Connection conn, String sqL) {
        PreparedStatement lPreparedStmt = null;
        try {
            lPreparedStmt = conn.prepareStatement(sqL);
            lPreparedStmt.execute();
        } catch (SQLException ex) {
            System.out.println("lPreparedStmt.execute();loooooooooooooose" + ex);
        } finally {
            if (nonNull(lPreparedStmt)) {
                try {
                    lPreparedStmt.close();
                } catch (SQLException pEx) {
                    System.err.println("unrecoverable:" + pEx);
                }
            }
        }
    }
}
        