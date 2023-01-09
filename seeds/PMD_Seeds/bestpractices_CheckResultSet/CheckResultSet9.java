
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

public class Foo {
    public boolean bar(Connection con) {
        boolean result;
        if (checkIsCached()) {
            result = true;
        } else {
            try (PreparedStatement stmt = con.prepareStatement("select * from foo where a = ?1 and b = ?2")) {
                stmt.setString(1, "a");
                stmt.setLong(2, 1L);
                try (ResultSet queryResults = stmt.executeQuery()) { // HERE -> Violation
                    if (queryResults.next()) {
                        result = true;
                    } else {
                        result = false;
                    }
                }
            }
        }
        return result;
    }
}
        