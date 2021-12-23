
import java.sql.Connection;

public class Foo {
    void bar() {
        String test = "";
        if (test != null) {
            throw new RuntimeException("haha");
        }
        Connection c = pool.getConnection();
        // No sentences between creation and try, ok
        try {
        } catch (Exception e) {
        } finally {
            if (c!= null) {
                c.close();
            }
        }
    }
}
        