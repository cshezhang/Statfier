
public abstract class ShouldBeAbstract
{
    public boolean notAbstract(String str)
    {
        return Boolean.valueOf(str);
    }

    public int neitherThisOne(int i)
    {
        return i + 3;
    }

    public int neitherThisOne(String str)
    {
        return Integer.valueOf(str);
    }
}
        