
public class MyTestCase extends TestCase {
    public void testMyCaseWithAssertEqualsOnBoolean() {
        boolean myVar = true;
        assertEquals("myVar is true", true, myVar);
        assertEquals("myVar is true", myVar, true);
        assertEquals("myVar is true", false, myVar);
        assertEquals("myVar is true", myVar, false);
        assertEquals(true, myVar);
    }
}
        