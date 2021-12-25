
public class Foo
{
    public void testMethod(String str)
    {
        if (str.equals(getAnotherString("abc"))) {}
    }

    private String getAnotherString(String str)
    {
        return "xyz";
    }
}
        