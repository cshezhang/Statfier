
public class MyTestCase extends TestCase {
    //positive on rule
    public void testMyCaseWithMoreAsserts() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }
}
        