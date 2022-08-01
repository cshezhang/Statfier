
import java.sql.Connection;

public class Test {
    public Test() {
        Connection c;
        try {
            c = pool.getConnection();
        } finally {
            c.close();
        }
    }
}
        