
import java.sql.Connection;

public class Foo {
    void bar() {
        Connection c = pool.getConnection();
        try {
        } catch (Exception e) {
        } finally {
            c.close();
        }
    }
}
        