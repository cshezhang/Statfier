
public class Foo
{
    public void testMethod(String str)
    {
        if (equalsIgnoreCase(getAnotherString("abc"))){}
    }

    private String getAnotherString(String str)
    {
        return "xyz";
    }
}
        