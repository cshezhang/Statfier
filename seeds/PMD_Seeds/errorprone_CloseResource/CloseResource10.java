
import java.sql.*;
public class Foo {
    void bar() {
        Connection c;
        try {
            c = pool.getConnection();
        } finally {
            this.closeConnection(null);
        }
    }
}
        