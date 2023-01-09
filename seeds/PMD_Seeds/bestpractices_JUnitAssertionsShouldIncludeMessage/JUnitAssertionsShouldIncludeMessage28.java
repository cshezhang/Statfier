
public class Foo {
    @org.junit.Test
    public void foo() {
        org.junit.Assert.assertEquals(1, 1);
        Foo.fail();
    }
    private static void fail() { }
}
        