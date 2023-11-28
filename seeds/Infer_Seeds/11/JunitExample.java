import junit.framework.TestCase;

public class JunitExample extends TestCase {

  private Object mField;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mField = new Object();
  }

  public void testSomething() {
    mField.toString();
  }
}

