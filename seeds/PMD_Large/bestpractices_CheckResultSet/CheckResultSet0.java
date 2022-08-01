
public class ResultSetTesting
{
    public String goodBehavior() throws SQLException
    {
        String firsString = "";
        Statement stat = (Statement) conn.createStatement();
        ResultSet rst = stat.executeQuery("SELECT name FROM person");

        if (rst.next())
        {
            firsString = rst.getString(1);
        }
        else
        {
            // deal properly with the problem..
        }
        return firsString;
    }
}
        