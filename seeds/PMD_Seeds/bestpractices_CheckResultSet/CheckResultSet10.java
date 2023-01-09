
import java.sql.ResultSet;

public class Foo {
    public void firstMethod() {
        ResultSet results = stmt.executeQuery();
        if (results.next()) {
            // do something
        }
    }

    public void secondMethod() {
        MyIterator results = getMyResultsIterator();
        results.next();
    }
}
        