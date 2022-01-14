
public class NoTypeParameterHiding<T> {

    public class Inner<S> { // Compliant
      List<S> listOfS;
    }

    private <V> V method() { // Compliant
      return null;
    }

  }
