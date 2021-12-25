
public class Foo extends TestCase{
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests");
        suite.addTestSuite(MyTest.class);
        return suite;
    }
    @Test
    public void foo() {
    }
}
        