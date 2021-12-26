
public class Foo {
  public void bar() {
    try {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ps = getCurrentSession().connection().prepareStatement(query);
        ps.setInt(1, fiscalYear);
        rs = ps.executeQuery();
        if (rs.next()) {
            result = rs.getInt("value");
        }
    } catch (SQLException se) {
        throw new DataAccessException(se);
    }
    return result;
  }
}
        