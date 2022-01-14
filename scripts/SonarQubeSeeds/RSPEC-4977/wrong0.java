
 public class TypeParameterHidesAnotherType<T> {

    public class Inner<T> { // Noncompliant
      //...
    }

    private <T> T method() { // Noncompliant
      return null;
    }

  }
