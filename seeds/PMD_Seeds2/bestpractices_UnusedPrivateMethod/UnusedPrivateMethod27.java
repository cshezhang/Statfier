
public class Foo {
    private String s;

    public Foo() {
        s = "Foobar";
    }

    //This method isn't flagged
    private void bar(int... n)
    {
    }

    //This method is flagged
    private void bar(String s)
    {
    }

    public void dummyMethod() {
        bar(s);
    }
}
        