
import com.special.ResultSet;

public class Foo {
  public void bar() {
    ResultSet rs = doExecuteQuery();
    rs.next(); // no warning here because it's not a java.sql.ResultSet
    result = rs.getInt("baz");
  }
}
        