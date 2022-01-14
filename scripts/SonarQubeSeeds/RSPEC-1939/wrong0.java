
public interface MyFace {
  // ...
}

public interface MyOtherFace extends MyFace {
  // ...
}

public class Foo
    extends Object // Noncompliant
    implements MyFace, MyOtherFace {  // Noncompliant
  //...
}
