
// without the import
//import java.sql.*;
// type resolution doesn't work, so the rule falls back to simple names
public class Foo {
    void bar() {
        Connection c = pool.getConnection();
        try {
        } catch (Exception e) {
        }
    }
}
        