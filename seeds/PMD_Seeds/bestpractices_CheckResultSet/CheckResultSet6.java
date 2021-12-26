
import java.sql.ResultSet;

public class Foo {
  public void bar() {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        ps = getCurrentSession().connection().prepareStatement(query);
        ps.setInt(1, fiscalYear);
        rs = ps.executeQuery();
        rs.next(); //this should be a PMD warning, but it is not
        result = rs.getInt("value");
    } catch (SQLException se) {
        throw new DataAccessException(se);
    } finally {
        //call method to close the ResultSet and PreparedStatement
    }
    return result;
  }
}
        