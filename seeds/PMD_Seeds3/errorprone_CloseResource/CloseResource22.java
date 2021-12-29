
import java.sql.Connection;

public class Foo {
    void bar() {
        String test = "";
        Connection c = pool.getConnection();
        if (test != null) {
            throw new RuntimeException("haha"); // <- RuntimeException, Connection  c is not closed
        }
        try {
        } catch (Exception e) {
        } finally {
           c.close();
        }
    }
}
        