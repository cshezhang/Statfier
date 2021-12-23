
public class Foo {
    private boolean isAorB(MyEnum value)
    {
      switch (value)
      {
        case A:
        case B:
        {
          return true;
        }
        default:
        {
          return false;
        }
      }
    }
}
        