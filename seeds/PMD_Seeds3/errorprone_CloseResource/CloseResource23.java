
import java.sql.Connection;

public class Foo {
    void bar() {
        String test = "";
        Connection c = null;
        if (test != null) {
            throw new RuntimeException("haha");
        }
        try {
            // Creation inside try, ok
            c = pool.getConnection();
        } catch (Exception e) {
        } finally {
            if (c!= null) {
                c.close();
            }
        }
    }
}
        