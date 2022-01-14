
public class MyClassTest extends MyAbstractTestCase {

  private MyClass myClass;
    @Override
    protected void setUp() throws Exception {
      super.setUp();
      myClass = new MyClass();
    }
