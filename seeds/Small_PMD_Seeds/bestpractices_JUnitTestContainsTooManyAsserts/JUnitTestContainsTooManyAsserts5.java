
public class MyTestCase extends TestCase {
    //positive on rule
    @Test
    public void myCaseWithMoreAsserts() {
        boolean myVar = false;
        assertFalse("myVar should be false",myVar);
        assertEquals("should equals false", false, myVar);
    }
}
        