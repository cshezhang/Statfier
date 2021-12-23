
public class Foo extends TestCase{
    public static Test suite() {
        return new JUnit4TestAdapter(Foo.class);
    }
    @Test
    public void foo() {
    }
}
        