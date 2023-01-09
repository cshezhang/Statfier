
public class Ineffecient
{
    public static String getValue(int in)
    {
        Integer i = new Integer(in);
        if (i.toString().length() == 1)
        {
            return "length 1";
        }
        return "";
    }

    public String toString()
    {
        return "Ineffecient";
    }
}
        