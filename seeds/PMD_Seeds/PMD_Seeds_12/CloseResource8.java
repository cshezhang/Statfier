
import java.sql.*;
public class Foo {
    public void bar() throws SQLException
    {
        DAOTransaction trx = trxManager.open();
        Connection cnx = pool.newConn();
        ResultSet rs = null;
        Statement stmt = null;

        try
        {
            // ...
        }
        finally
        {
            //stmt.close(); // Error !!! you have to close the Statement
            rs.close(); // Correct
            cnx.commit(); // Correct ( cnx.close() would be equivalent)
            trx.commit(); // Correct
        }
    }
}
        