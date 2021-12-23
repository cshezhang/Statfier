
public class Test {
    public int countReadOnlyForwardOnlyJDBC() throws SQLException, ClassNotFoundException {
        int _count = 0;

        if (conn == null) {
            conn = getConnection();
            if (conn == null) {
                return null;
            }
        }

        if (stmt == null) {
            stmt = conn.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        }

        ResultSet _rs = stmt.executeQuery(QueryString);

        while (_rs.next() != false) {
            _count++;
        }

        return _count;
    }

    public int countReadOnlyForwardOnlyJDBC2() throws SQLException, ClassNotFoundException {
        int _count = 0;

        if (conn == null) {
            conn = getConnection();
            if (conn == null) {
                return null;
            }
        }

        if (stmt == null) {
            stmt = conn.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        }

        ResultSet _rs = stmt.executeQuery(QueryString);
        _rs.next();  // This line should throw a PMD violation.  < - - - violation here
        while (_rs.next() != false) {
            _count++;
        }

        return _count;
    }
}
        