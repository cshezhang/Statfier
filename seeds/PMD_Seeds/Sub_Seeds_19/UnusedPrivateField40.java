
public class IssueUnusedPrivateField {

    private static Object helper;   // PMD warns unused

    @BeforeClass
    public static void setUpClass() {
        helper = new Object();
    }

    @Test
    public void testSomething() {
        String str = helper.toString();     // used here
        System.out.println("str = " + str);

        String helper = "some new string";  // hidden here
        System.out.println("helper = " + helper);
    }
}
        