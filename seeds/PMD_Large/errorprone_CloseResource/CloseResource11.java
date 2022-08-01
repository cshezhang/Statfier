
import java.sql.*;
public class Foo {
    void bar() {
        Connection c = pool.getConnection();
        try {
            //...
        } finally {
            this.closeConnection(null);
        }
    }
}
        