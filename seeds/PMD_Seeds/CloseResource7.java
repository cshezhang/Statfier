package iter0;

import java.sql.*;
public class Foo {
    void bar() {
        Statement c = pool.getStmt();
        try {
        } finally {
            MyHelper.close(c);
        }
    }
}
        