
import java.util.*;

public class PMDDemo {
    public void checkArray() {
        Car[] cars = new Car[3];
        for(int i = 0; i < cars.length; ++i) {
           cars[i] = new Car();
        }
    }
    public void checkCollection() {
        Collection<Car> cars = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
           cars.add(new Car());
        }
    }
}
        