
public class ResultSetTesting
{
    public void executeSql(Statement statement, String query) throws SQLException
    {
        ResultSet rst = stat.executeQuery("SELECT name FROM person");
        // stupid while loop to have an unrelated while
        while (_count > 0) {
            _count--;
        }
        while (rst.next())
        {
            firsString = rst.getString(1);
        }
    }
}
        