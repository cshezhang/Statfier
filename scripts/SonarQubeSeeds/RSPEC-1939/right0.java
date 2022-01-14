
public interface MyFace {
  // ...
}

public interface MyOtherFace extends MyFace {
  // ...
}

public class Foo implements MyOtherFace {
  //...
}
