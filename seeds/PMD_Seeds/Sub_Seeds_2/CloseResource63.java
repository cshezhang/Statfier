
import java.sql.Connection;

public class Foo {
    void bar() {
        Connection c = pool.getConnection();
        try {
          c.close();
          c = pool.getConnection();
        } catch (Exception e) {
        } finally {
            c.close();
        }
    }
}
        