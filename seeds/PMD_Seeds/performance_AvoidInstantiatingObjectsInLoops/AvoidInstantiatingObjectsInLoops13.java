import java.util.ArrayList;
import java.util.Collection;

public class Sample {
  public Collection<Car> checkCollection() {
    Collection<Car> thisIsACar = new ArrayList<>();
    for (int i = 0; i < 3; ++i) {
      thisIsACar.add(new Car());
    }
    return thisIsACar;
  }

  private static class Car {}
}

