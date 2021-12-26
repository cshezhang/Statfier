
import java.sql.ResultSet;

public class Foo {
    public boolean bar() {
        ResultSet results = stmt.executeQuery();
        return results.next();
    }
}
        