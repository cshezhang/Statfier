
public class MyTestCase extends TestCase {
    //negative on rule
    @Test
    public void myCaseWithOneAssert() {
        boolean myVar = false;
        assertFalse("should be false",myVar);
    }
}
        