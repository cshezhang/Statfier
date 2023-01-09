import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

class DumbMethodInvocations {

    @ExpectWarning("DMI_EMPTY_DB_PASSWORD")
    Connection getConnection1() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
    }

}
