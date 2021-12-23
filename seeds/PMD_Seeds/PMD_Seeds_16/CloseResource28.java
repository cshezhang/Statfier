
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class CloseResourceTest {
    public Object selectOne(final int val, final int val2) {
        getCurrentSession().doWork(new Work() {

            @Override
            public void execute(Connection connection) throws SQLException {
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = createDefaultPreparedStatement(connection, CONSTANTS.QUERY_STRING);
                    stmt.setInt(1, vaL);
                    stmt.setInt(2, val2);
                    rs = stmt.executeQuery();
                    // do result set processing
                } catch (SQLException se) {
                    // error
                } finally {
                    closeLocalResources(stmt);
                    //closeResultSet(rs); // --- ERROR
                }
            }
        });
    }
}
        