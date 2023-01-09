
import java.sql.Connection;

public class Bar {
    public void foo() {
        Connection c = pool.getConnection();
        try {
            // do stuff
        } catch (SQLException ex) {
            // handle exception
        } finally {
            if(false) {
                c.close();
            }
        }
    }
}
        