
public class ResultSetTesting
{
    public void executeSql(Statement statement, String query) throws SQLException
    {
        ResultSet rs = statement.executeQuery(query);
        String object = rs.getString(1);
        rs.first();
        object.hashCode();
    }
}
        